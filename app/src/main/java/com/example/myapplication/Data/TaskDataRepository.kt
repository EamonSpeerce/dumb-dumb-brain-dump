package com.example.myapplication.Data

import com.example.myapplication.Domain.Task
import com.example.myapplication.Domain.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TaskDataRepository(private val dao: TaskDao): TaskRepository {
    override fun getTask(): Flow<List<Task>> {
        return dao.getAllRepeatTasks().map {  list ->
            list.map { entity ->
                Task(entity.id, entity.name, entity.description, entity.repeatPeriod, entity.completedActions, entity.currentStreak)
            }
        }
    }

    override suspend fun addTask(task: Task) {
        dao.insert(TaskEntity(task.id, task.name, task.description, task.repeatPeriod, task.completedActions, task.currentStreak))
    }

    override suspend fun removeTask(task: Task) {
        dao.delete(TaskEntity(task.id, task.name, task.description, task.repeatPeriod, task.completedActions, task.currentStreak))
    }

    override suspend fun updateTask(task: Task) {
        dao.update(TaskEntity(task.id, task.name, task.description, task.repeatPeriod, task.completedActions, task.currentStreak))
    }


}