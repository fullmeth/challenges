package com.mentorship.project

import android.content.Context
import com.mentorship.project.api.DummyDto
import com.mentorship.project.api.RetrofitService
import com.mentorship.project.utils.Result
import com.mentorship.project.utils.safeApiCall

class RepositoryImpl(context: Context) : Repository {

    private val dummyPreferences = DummyPreferences(context)
    private val retrofit = RetrofitService

    override suspend fun getDummyResponse(
        code: Int,
        delay: Int?
    ): Result<DummyDto> = safeApiCall {
        retrofit.dummyApiService().getDummyJson(code, delay)
    }

    @Throws(IllegalArgumentException::class)
    override fun savePreference(
        type: SharedPreferenceType,
        value: String
    ) {
        when (type) {
            SharedPreferenceType.STRING -> dummyPreferences.string = value
            SharedPreferenceType.INT -> dummyPreferences.int = value.toInt()
            SharedPreferenceType.LONG -> dummyPreferences.long = value.toLong()
            SharedPreferenceType.FLOAT -> dummyPreferences.float = value.toFloat()
            SharedPreferenceType.BOOLEAN -> dummyPreferences.boolean =
                value.toBooleanStrict()

            SharedPreferenceType.STRING_SET -> dummyPreferences.stringSet =
                value.split(", ").toSet()
        }
    }

    override fun getPreference(type: SharedPreferenceType): String = when (type) {
        SharedPreferenceType.STRING -> dummyPreferences.string
        SharedPreferenceType.INT -> dummyPreferences.int.toString()
        SharedPreferenceType.LONG -> dummyPreferences.long.toString()
        SharedPreferenceType.FLOAT -> dummyPreferences.float.toString()
        SharedPreferenceType.BOOLEAN -> dummyPreferences.boolean.toString()
        SharedPreferenceType.STRING_SET -> dummyPreferences.stringSet.joinToString(", ")

    }
}