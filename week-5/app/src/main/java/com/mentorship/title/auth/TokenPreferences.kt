package com.mentorship.title.auth

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.mentorship.title.utils.PreferenceDelegate

class TokenPreferences(context: Context) {

    private val prefs = context.getSharedPreferences("Token", MODE_PRIVATE)

    var refreshToken by PreferenceDelegate(prefs, "refreshToken", "")
    var accessToken by PreferenceDelegate(prefs, "accessToken", "")
}