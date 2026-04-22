package com.example.dumb_dumb_brain_dump.Data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [TaskEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class TaskDatabase: RoomDatabase() {

    abstract fun taskDao(): TaskDao
}