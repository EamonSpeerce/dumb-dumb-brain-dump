package com.example.dumb_dumb_brain_dump

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.dumb_dumb_brain_dump.View.TaskViewModel
import com.example.dumb_dumb_brain_dump.View.screens.TaskScreen

class MainActivity : ComponentActivity() {

    private val taskViewModel: TaskViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme{
                Surface{
                    TaskScreen(taskViewModel = taskViewModel)
                }
            }

        }
    }
}
