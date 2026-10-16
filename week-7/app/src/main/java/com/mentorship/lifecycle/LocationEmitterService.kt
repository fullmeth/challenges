package com.mentorship.lifecycle

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.location.Location
import android.os.Binder
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.core.content.PermissionChecker
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

class LocationEmitterService : Service() {

    private val serviceScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val binder = LocationEmitterBinder()
    private lateinit var client: FusedLocationProviderClient
    private val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5_000L).build()
    private var backgroundJob: Job? = null
    private lateinit var locationState: LocationStatePreferences
    private val notificationId = 333
    private val channelId = "location_channel_id"

    private fun createNotification(location: Location? = null): Notification {
        return NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle("Location tracking active")
            .setContentText(
                location?.let {
                    "Lat: ${it.latitude}, Lon: ${it.longitude}"
                } ?: "Waiting for location..."
            )
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .build()
    }

    private fun updateNotification(location: Location) {
        val notificationManager = getSystemService(NotificationManager::class.java)

        notificationManager.notify(
            notificationId,
            createNotification(location)
        )
    }

    override fun onCreate() {
        super.onCreate()
        client = LocationServices.getFusedLocationProviderClient(this)
        locationState = LocationStatePreferences(this)
    }

    private fun startForeground(): Boolean {
        val accessFineLocationPermission =
            PermissionChecker.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        val accessCoarseLocationPermission =
            PermissionChecker.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        if (accessFineLocationPermission != PermissionChecker.PERMISSION_GRANTED && accessCoarseLocationPermission != PermissionChecker.PERMISSION_GRANTED) {
            stopSelf()
            return false
        }

        return try {
            createNotificationChannel()
            ServiceCompat.startForeground(
                /* service = */ this,
                /* id = */ notificationId,
                /* notification = */ createNotification(),
                /* foregroundServiceType = */ ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION,
            )
            true
        } catch (e: Exception) {
            Log.e(
                "LocationEmitterService",
                "Something went terribly wrong. Cause: ${e.message}"
            )
            stopSelf()
            false
        }
    }

    private fun createNotificationChannel() {
        val name = "LocationChannelName"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, name, importance)
        val notificationManager: NotificationManager =
            getSystemService(NotificationManager::class.java) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private val locationFlow = callbackFlow {
        if (!hasLocationPermission()) {
            close(SecurityException("Location permission not granted"))
            return@callbackFlow
        }

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { trySend(it) }
            }
        }

        try {
            val task = client.requestLocationUpdates(
                request,
                locationCallback,
                Looper.getMainLooper()
            )
            task.addOnFailureListener { close(it) }

            awaitClose {
                client.removeLocationUpdates(locationCallback)
            }
        } catch (e: SecurityException) {
            close(e)
        }
    }.shareIn(serviceScope, SharingStarted.WhileSubscribed(5_000L), 1)

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int {
        if (intent?.action == ACTION_STOP_TRACKING ||
            !locationState.isBackgroundTrackingEnabled
        ) {
            backgroundJob?.cancel()
            backgroundJob = null

            stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelf()

            return START_NOT_STICKY
        }

        if (backgroundJob?.isActive != true) {
            if (startForeground()) {
                backgroundJob = serviceScope.launch {
                    locationFlow.collect {
                        updateNotification(it)
                    }
                }
            } else {
                return START_NOT_STICKY
            }
        }

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun onDestroy() {
        serviceScope.cancel()
        super.onDestroy()
    }

    inner class LocationEmitterBinder : Binder() {

        fun getService() = this@LocationEmitterService

        fun getLocationFlow() = this@LocationEmitterService.locationFlow
    }

    companion object {
        const val ACTION_STOP_TRACKING = "ACTION_STOP_TRACKING"
    }
}