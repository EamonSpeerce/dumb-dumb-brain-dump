package com.example.dumb_dumb_brain_dump.Data

import androidx.room.TypeConverter
import java.time.Instant
import java.util.ArrayDeque
import java.util.Deque

class Converters {

    @TypeConverter
    fun fromDeque(deque: Deque<Instant>): String {
        return deque.joinToString(",") { it.toEpochMilli().toString() }
    }

    @TypeConverter
    fun toDeque(value: String): Deque<Instant> {
        if (value.isBlank()) return ArrayDeque()
        return value.split(",")
            .map { Instant.ofEpochMilli(it.toLong()) }
            .let { ArrayDeque(it) }
    }


}