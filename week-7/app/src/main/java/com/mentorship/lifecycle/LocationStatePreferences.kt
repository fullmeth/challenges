package com.mentorship.lifecycle

import android.content.Context
import android.content.Context.MODE_PRIVATE

class LocationStatePreferences(context: Context) {

    private val prefs = context.getSharedPreferences("location", MODE_PRIVATE)

    var isBackgroundTrackingEnabled by PreferenceDelegate(prefs, "isBackgroundTrackingEnabled", false)
}