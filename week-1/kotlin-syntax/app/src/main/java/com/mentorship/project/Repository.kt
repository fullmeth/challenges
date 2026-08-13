package com.mentorship.project

import com.mentorship.project.api.DummyDto
import com.mentorship.project.utils.Result

interface Repository {

    suspend fun getDummyResponse(code: Int, delay: Int? = null): Result<DummyDto>

    fun savePreference(type: SharedPreferenceType, value: String)

    fun getPreference(type: SharedPreferenceType): String
}