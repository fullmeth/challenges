package com.mentorship.project.utils

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

@Suppress("UNCHECKED_CAST")
class PreferenceDelegate<T>(
    private val prefs: SharedPreferences,
    private val key: String,
    private val default: T,
) : ReadWriteProperty<Any?, T> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): T = when (default) {
        is String -> prefs.getString(key, default) as T
        is Int -> prefs.getInt(key, default) as T
        is Long -> prefs.getLong(key, default) as T
        is Float -> prefs.getFloat(key, default) as T
        is Boolean -> prefs.getBoolean(key, default) as T
        is Set<*> -> prefs.getStringSet(key, default as Set<String>) as T
        else -> throw IllegalArgumentException("not supported")
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        prefs.edit {
            when (value) {
                is String -> putString(key, value)
                is Int -> putInt(key, value)
                is Long -> putLong(key, value)
                is Float -> putFloat(key, value)
                is Boolean -> putBoolean(key, value)
                is Set<*> -> putStringSet(key, value.map { it.toString() }.toSet())
                else -> throw IllegalArgumentException("not supported")
            }
        }
    }
}