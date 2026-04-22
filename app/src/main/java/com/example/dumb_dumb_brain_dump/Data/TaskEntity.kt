package com.example.dumb_dumb_brain_dump.Data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant
import java.util.ArrayDeque
import java.util.Deque

@Entity(tableName = "repeatTasks")
data class TaskEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,
    val description: String,

    val repeatPeriod: Long,

    val completedActions: Deque<Instant> = ArrayDeque(),

    val currentStreak: Int
)