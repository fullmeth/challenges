package com.mentorship.lifecycle

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mentorship.lifecycle.ui.theme.LifecycleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LifecycleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        val context = LocalContext.current
                        val lifecycleOwner = LocalLifecycleOwner.current
                        val locationLifecycleEventObserver = remember(lifecycleOwner) {
                            LocationLifecycleEventObserver(
                                context,
                                lifecycleOwner
                            )
                        }
                        var pendingPermissionForeground by remember { mutableStateOf(false) }
                        val preferenceDelegate = remember { LocationStatePreferences(context) }
                        var foregroundSwitch by remember { mutableStateOf(false) }
                        var backgroundSwitch by remember { mutableStateOf(preferenceDelegate.isBackgroundTrackingEnabled) }
                        val intent = Intent(
                            context,
                            LocationEmitterService::class.java
                        )
                        val flow by locationLifecycleEventObserver.flow.collectAsStateWithLifecycle(
                            initialValue = null
                        )
                        val permissionLauncher = rememberLauncherForActivityResult(
                            contract = ActivityResultContracts.RequestMultiplePermissions()
                        ) { permissions ->
                            val granted = permissions.values.any { it }

                            if (granted) {
                                if (pendingPermissionForeground) {
                                        foregroundSwitch = true
                                        locationLifecycleEventObserver.setTrackingEnabled(true)
                                    } else {
                                        preferenceDelegate.isBackgroundTrackingEnabled = true
                                        ContextCompat.startForegroundService(
                                            context,
                                            Intent(context, LocationEmitterService::class.java)
                                        )
                                        backgroundSwitch = true
                                    }
                                }
                            }
                        DisposableEffect(locationLifecycleEventObserver) {
                            lifecycleOwner.lifecycle.addObserver(locationLifecycleEventObserver)
                            onDispose {
                                locationLifecycleEventObserver.cleanup()
                                lifecycleOwner.lifecycle.removeObserver(
                                    locationLifecycleEventObserver
                                )
                            }
                        }
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = spacedBy(12.dp)
                        ) {
                            Text(text = "Current Location: ")
                            SwitchRow(
                                modifier = Modifier.fillMaxWidth(),
                                text = "Foreground location: ${flow?.latitude} lat ${flow?.longitude} lon",
                                checked = foregroundSwitch,
                                onCheckedChange = { enabled ->
                                    if (!enabled) {
                                        foregroundSwitch = false
                                        locationLifecycleEventObserver.setTrackingEnabled(false)
                                    } else if (hasLocationPermission()) {
                                        foregroundSwitch = true
                                        locationLifecycleEventObserver.setTrackingEnabled(true)
                                    } else {
                                        pendingPermissionForeground = true
                                        permissionLauncher.launch(
                                            arrayOf(
                                                Manifest.permission.ACCESS_FINE_LOCATION,
                                                Manifest.permission.ACCESS_COARSE_LOCATION
                                            )
                                        )
                                    }
                                }
                            )
                            SwitchRow(
                                modifier = Modifier.fillMaxWidth(),
                                text = "Background location: ${flow?.latitude} lat ${flow?.longitude} lon",
                                checked = backgroundSwitch,
                                onCheckedChange = { enabled ->
                                    if (!enabled) {
                                        backgroundSwitch = false
                                        preferenceDelegate.isBackgroundTrackingEnabled = false
                                        context.startService(
                                            Intent(context, LocationEmitterService::class.java).apply {
                                                action = LocationEmitterService.ACTION_STOP_TRACKING
                                            }
                                        )
                                    } else if (hasLocationPermission()) {
                                        backgroundSwitch = true
                                        preferenceDelegate.isBackgroundTrackingEnabled = true
                                        ContextCompat.startForegroundService(context, Intent(context, LocationEmitterService::class.java))
                                    } else {
                                        pendingPermissionForeground = false
                                        permissionLauncher.launch(
                                            arrayOf(
                                                Manifest.permission.ACCESS_FINE_LOCATION,
                                                Manifest.permission.ACCESS_COARSE_LOCATION
                                            )
                                        )
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SwitchRow(
    modifier: Modifier = Modifier,
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(modifier = modifier, horizontalArrangement = spacedBy(12.dp)) {
        Switch(checked = checked, onCheckedChange = onCheckedChange)
        Text(text = text)
    }
}

fun Context.hasLocationPermission(): Boolean {
    return listOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ).any {
        ContextCompat.checkSelfPermission(
            this,
            it
        ) == PackageManager.PERMISSION_GRANTED
    }
}

