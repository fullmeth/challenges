package com.mentorship.persistence

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        cleanUpTempFiles()
    }

    private fun cleanUpTempFiles() {
        CoroutineScope(Dispatchers.IO).launch {
            applicationContext.filesDir?.listFiles {
                it.name.endsWith(".tmp")
            }?.forEach {
                it.deleteRecursively()
            }
        }
    }
}