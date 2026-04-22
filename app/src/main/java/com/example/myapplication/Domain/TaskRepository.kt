package com.example.myapplication.Domain

import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    fun getTask(): Flow<List<Task>>
    suspend fun addTask(task: Task)
    suspend fun removeTask(task: Task)
    suspend fun updateTask(task: Task)



}