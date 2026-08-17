package com.mentorship.project

import com.mentorship.project.api.DummyModel
import com.mentorship.project.utils.Result

interface Repository {

    suspend fun getDummyResponse(code: Int, delay: Int? = null): Result<DummyModel>

    fun savePreference(type: SharedPreferenceType, value: String)

    fun getPreference(type: SharedPreferenceType): String
}