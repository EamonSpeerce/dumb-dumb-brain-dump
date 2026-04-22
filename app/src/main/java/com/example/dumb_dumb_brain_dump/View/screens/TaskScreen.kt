package com.example.dumb_dumb_brain_dump.View.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LibraryAddCheck
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.dumb_dumb_brain_dump.Data.TimeUtils
import com.example.dumb_dumb_brain_dump.Data.TimeUtils.properNoun
import com.example.dumb_dumb_brain_dump.Domain.Task
import com.example.dumb_dumb_brain_dump.View.TaskViewModel
import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(taskViewModel: TaskViewModel) {

    var taskName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var periodCountText by remember { mutableStateOf("1") }
    var periodCount by remember { mutableIntStateOf(1) }
    var selectedUnit by remember { mutableStateOf(ChronoUnit.DAYS) }
    val taskList by taskViewModel.tasks.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Repeated Tasks") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Red,
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(2.dp, Color.Black),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(2f)) {
                        OutlinedTextField(
                            value = taskName,
                            onValueChange = { taskName = it },
                            label = { Text("Task Name") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("Task Description") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = periodCountText,
                            onValueChange = { input ->
                                // Always accept the raw text so backspace/empty works naturally
                                periodCountText = input.filter { it.isDigit() }

                                // Only update the real value when it parses to a valid range
                                val parsed = periodCountText.toIntOrNull()
                                if (parsed != null && parsed in 1..999) periodCount = parsed
                            },
                            label = { Text("Repeat Every") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                        PeriodUnitSelector(
                            selected = selectedUnit,
                            onSelected = { selectedUnit = it }
                        )
                    }
                }
                Row() {
                    IconButton(
                        onClick = {
                            if (taskName.isNotBlank()) {
                                taskViewModel.addTask(
                                    name = taskName,
                                    desc = description,
                                    repeatPeriod = TimeUtils.periodToMillis(periodCount, selectedUnit)
                                )
                                taskName = ""
                                description = ""
                                periodCount = 1
                                selectedUnit = ChronoUnit.DAYS
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),

                    ) {
                        Icon(Icons.Default.LibraryAddCheck, contentDescription = "Add Task")
                    }
                }
            }

            HorizontalDivider(color = Color.Black, thickness = 2.dp)

            LazyVerticalGrid(
                columns = GridCells.Fixed(1),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(items = taskList, key = { it.id }) { task ->
                    TaskCard(task = task, taskViewModel = taskViewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeriodUnitSelector(
    selected: ChronoUnit,
    onSelected: (ChronoUnit) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
        OutlinedTextField(
            value = selected.toString(),
            onValueChange = {},
            readOnly = true,
            modifier = Modifier.menuAnchor(type = ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled = true)
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            TimeUtils.PERIOD_UNITS.forEach { unit ->
                DropdownMenuItem(
                    text = { Text(unit.toString().properNoun()) },
                    onClick = { onSelected(unit); expanded = false }
                )
            }
        }
    }
}

@Composable
fun TaskCard(task: Task, taskViewModel: TaskViewModel) {
    var isEditing by remember(task.id) { mutableStateOf(false) }
    var newTitle by remember(task.id) { mutableStateOf(task.name) }
    var newDescription by remember(task.id) { mutableStateOf(task.description) }
    var newRepeatPeriod by remember(task.id) { mutableStateOf(task.repeatPeriod) }

    val daysUntilDeadline = remember(task.nextActionDeadline) {
        if (task.nextActionDeadline == Instant.MAX) null
        else Duration.between(Instant.now(), task.nextActionDeadline).toDays()
    }

    val borderColor = if (task.isPastActionDeadline) Color.Red else Color.Black

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(2.dp, borderColor),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isEditing) {
                    OutlinedTextField(
                        value = newTitle,
                        onValueChange = { newTitle = it },
                        label = { Text("Name") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )
                } else {
                    Text(
                        text = task.name,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (task.isPastActionDeadline) Color.Red else Color.Unspecified
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }
                Row {
                    IconButton(onClick = {
                        if (!isEditing) {
                            newTitle = task.name
                            newDescription = task.description
                        }
                        isEditing = !isEditing
                    }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                    IconButton(onClick = { taskViewModel.deleteTask(task) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                    }
                }
            }

            if (isEditing) {
                OutlinedTextField(
                    value = newDescription,
                    onValueChange = { newDescription = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )
            } else {
                Text(
                    text = task.description,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = TimeUtils.millisToReasonableTimeLabel(task.repeatPeriod),
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = when {
                        daysUntilDeadline == null -> "Not started yet"
                        daysUntilDeadline < 0 -> "${-daysUntilDeadline} days overdue"
                        daysUntilDeadline == 0L -> "Due today"
                        else -> "Next deadline in $daysUntilDeadline days"
                    },
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = if (task.isPastActionDeadline) Color.Red else Color.Unspecified
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (isEditing) {
                Button(
                    onClick = {
                        taskViewModel.editTask(
                            task.copy(
                                name = newTitle,
                                description = newDescription,
                                repeatPeriod = newRepeatPeriod
                            )
                        )
                        isEditing = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Save")
                }
            } else {
                Button(
                    onClick = { taskViewModel.completeTask(task) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                    border = BorderStroke(2.dp, Color.Black)
                ) {
                    Text("Complete Task")
                }
            }
        }
    }
}
