package com.mentorship.project

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.mentorship.project.utils.PreferenceDelegate

class DummyPreferences(context: Context) {

    private val prefs = context.getSharedPreferences("Dummy", MODE_PRIVATE)

    var string by PreferenceDelegate(prefs, "string", "default_value")
    var int by PreferenceDelegate(prefs, "int", -1)
    var long by PreferenceDelegate(prefs, "long", -100L)
    var float by PreferenceDelegate(prefs, "float", -1.0f)
    var boolean by PreferenceDelegate(prefs, "boolean", false)
    var stringSet by PreferenceDelegate(prefs, "stringSet", emptySet<String>())
}