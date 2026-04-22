package com.example.dumb_dumb_brain_dump.Domain

class AddTask(private val taskRepository: TaskRepository) {

    suspend fun execute(title: String, description: String, repeatPeriod_ms: Long) {
        taskRepository.addTask(Task(
            name = title, description = description, repeatPeriod = repeatPeriod_ms,
        ))
    }

}