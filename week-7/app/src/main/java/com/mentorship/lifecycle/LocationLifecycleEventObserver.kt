@file:OptIn(ExperimentalCoroutinesApi::class)

package com.mentorship.lifecycle

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.location.Location
import android.os.IBinder
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update

class LocationLifecycleEventObserver(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner
) : LifecycleEventObserver {

    private var locationEmitterService: LocationEmitterService? = null
    private var isBound = false
    private var isTrackingEnabled = false

    private val _flow = MutableStateFlow<SharedFlow<Location>?>(null)
    val flow = _flow.asStateFlow().flatMapLatest { it ?: emptyFlow() }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(
            name: ComponentName?,
            service: IBinder?
        ) {
            locationEmitterService =
                (service as LocationEmitterService.LocationEmitterBinder).getService()
            _flow.update { service.getLocationFlow() }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            locationEmitterService = null
            _flow.update { null }
        }
    }

    fun setTrackingEnabled(enabled: Boolean) {
        isTrackingEnabled = enabled
        onTrackingStateChange()
    }

    fun cleanup() {
        if (isBound) {
            context.unbindService(serviceConnection)
            isBound = false
            locationEmitterService = null
            _flow.update { null }
        }
    }

    private fun onTrackingStateChange() {
        if (lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED) && isTrackingEnabled) {
            if (!isBound) {
                Intent(context, LocationEmitterService::class.java).also {
                    isBound =
                        context.bindService(it, serviceConnection, Context.BIND_AUTO_CREATE)
                }
            }
        } else {
            cleanup()
        }
    }


    override fun onStateChanged(
        source: LifecycleOwner,
        event: Lifecycle.Event
    ) {
        when (event) {
            Lifecycle.Event.ON_CREATE,
            Lifecycle.Event.ON_RESUME,
            Lifecycle.Event.ON_PAUSE,
            Lifecycle.Event.ON_DESTROY,
            Lifecycle.Event.ON_ANY -> Unit

            Lifecycle.Event.ON_START,
            Lifecycle.Event.ON_STOP -> onTrackingStateChange()
        }
    }
}