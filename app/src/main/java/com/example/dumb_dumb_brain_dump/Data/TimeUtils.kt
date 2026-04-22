package com.example.dumb_dumb_brain_dump.Data

import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit

object TimeUtils {

    val PERIOD_UNITS = listOf(ChronoUnit.YEARS, ChronoUnit.MONTHS, ChronoUnit.WEEKS, ChronoUnit.DAYS)
    fun Instant.isSameDayAs(time: Instant?): Boolean {
        if (time == null) return false

        return this.truncatedTo(ChronoUnit.DAYS).equals(time.truncatedTo(ChronoUnit.DAYS))
    }

    fun ChronoUnit.displayLabel(): String = when (this) {
        ChronoUnit.DAYS   -> "Day"
        ChronoUnit.WEEKS  -> "Week"
        ChronoUnit.MONTHS -> "Month"
        ChronoUnit.YEARS  -> "Year"
        else -> name.properNoun()
    }

    fun String.properNoun(): String = this.lowercase().replaceFirstChar { it.uppercase() }

    /**
     * Determines the number of milliseconds in a given period
     * @param count - Number of days, weeks, months, or years to calculate
     * @param unit  - days, weeks, months, or years
     */
    fun periodToMillis(count: Int, unit: ChronoUnit): Long {
        val zone = ZoneId.systemDefault()
        val now = Instant.now()
        val future = now.atZone(zone).plus(count.toLong(), unit).toInstant()
        return future.toEpochMilli() - now.toEpochMilli()
    }

    /**
     * Gets a reasonable time label for the given period in milliseconds
     * @param millis - The Time in milliseconds
     * @return - X Units, where Units is in [Years, Months, Weeks, Days], defaulting to days
     */
    fun millisToReasonableTimeLabel(millis: Long): String {
        val zone = ZoneId.systemDefault()
        val now = Instant.now()
        val future = Instant.ofEpochMilli(now.toEpochMilli() + millis)

        // TODO: Bugged never works
        for (unit in PERIOD_UNITS) {
            val count = unit.between(now.atZone(zone), future.atZone(zone))
            if (count > 0) {
                val check = now.atZone(zone).plus(count, unit).toInstant()
                if (check.toEpochMilli() == future.toEpochMilli()) {
                    val label = if (count == 1L) unit.displayLabel() else "${unit.displayLabel()}s"
                    return "$count $label"
                }
            }
        }
        // Fallback for values that don't align to a clean unit
        return "${millis / (24L * 60 * 60 * 1000)} Days"
    }


}

