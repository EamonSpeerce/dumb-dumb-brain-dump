package com.example.myapplication.Domain

import java.time.Instant
import java.util.ArrayDeque
import java.util.Deque
import java.time.Duration

data class Task (

    val id: Int = 0,

    val name: String,
    val description: String,

    val repeatPeriod: Long,

    val completedActions: Deque<Instant> = ArrayDeque(),

    val currentStreak: Int = 0
) {
    val lastCompletedAction: Instant?
        get() = completedActions.peek()

    val nextActionDeadline: Instant
        get() = lastCompletedAction?.plus(Duration.ofMillis(repeatPeriod))?: Instant.MAX

    val isPastActionDeadline: Boolean
        get() = nextActionDeadline.let { Instant.now().isAfter(it) } ?: false

}