package com.example.myapplication.Domain

import java.time.Instant
import java.util.Deque

class AddTask(private val taskRepository: TaskRepository) {

    suspend fun execute(title: String, description: String, repeatPeriod_ms: Long) {
        taskRepository.addTask(Task(
            name = title, description = description, repeatPeriod = repeatPeriod_ms,
        ))
    }

}