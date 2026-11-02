package com.mentorship.persistence

import androidx.room3.ColumnTypeConverter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

object Converters {
    @ColumnTypeConverter
    fun fromLocalDateTime(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(value) }
    }

    @ColumnTypeConverter
    fun dateToLocalDateTime(date: LocalDateTime?): String? {
        return date?.format(DateTimeFormatter.ISO_DATE_TIME)
    }
}
