package com.example.myapplication.View

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.myapplication.Data.DateConverter.isSameDayAs
import com.example.myapplication.Data.TaskDataRepository
import com.example.myapplication.Data.TaskDatabase
import com.example.myapplication.Domain.AddTask
import com.example.myapplication.Domain.Task
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant


open class TaskViewModel(application: Application): AndroidViewModel(application) {

    private val db = Room.databaseBuilder(
        application,
        TaskDatabase::class.java,
        "my_tasks_db"
    ).build()

    private val taskRepository = TaskDataRepository(db.taskDao())
    private val addUseCase = AddTask(taskRepository)

    open val tasks = taskRepository.getTask().stateIn(
        viewModelScope, SharingStarted.Lazily, emptyList()
    )

    fun addTask(name: String, desc: String, repeatPeriod: Long) {
        viewModelScope.launch {
            addUseCase.execute(name, desc, repeatPeriod)
        }
    }

    fun completeTask(task: Task) {
        viewModelScope.launch {

            val currentTime = Instant.now()


            val newStreakNumber = if (task.completedActions.peek() == null) {
                1
            } else if (currentTime.isAfter(task.nextActionDeadline)) {
                1
            } else if (currentTime.isSameDayAs(task.lastCompletedAction)){
                task.currentStreak
            } else {
                task.currentStreak + 1
            }

            task.completedActions.push(currentTime)
            taskRepository.updateTask(task.copy(completedActions = task.completedActions, currentStreak = newStreakNumber))
        }
    }

    fun editTask(task: Task) {
        viewModelScope.launch {
            taskRepository.updateTask(task.copy())
        }

    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskRepository.removeTask(task)
        }
    }

}