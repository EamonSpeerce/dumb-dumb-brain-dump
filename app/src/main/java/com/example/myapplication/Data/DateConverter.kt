package com.example.myapplication.Data

import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit

object DateConverter {

    private const val MILLIS_PER_DAY = 24L * 60 * 60 * 1000

    fun daysToMillis(days: Int): Long = days.toLong() * MILLIS_PER_DAY

    fun millisToDays(millis: Long): Long = millis / MILLIS_PER_DAY

    fun Instant.isSameDayAs(time: Instant?): Boolean {
        if (time == null) return false

        return this.truncatedTo(ChronoUnit.DAYS).equals(time.truncatedTo(ChronoUnit.DAYS))
    }

    fun instantToDeadlineString(deadline: Instant): String {
        val days = Duration.between(Instant.now(), deadline).toDays()
        return when {
            deadline == Instant.MAX -> "Not started yet"
            days < 0  -> "${-days} days overdue"
            days == 0L -> "Due today"
            else -> "In $days days"
        }
    }
}

