package com.example.alertdialog3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme

import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.alertdialog3.ui.theme.AlertDialog3Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTaskApp()
        }
    }
}

@Composable
fun MyTaskApp() {
    var showDialog by remember { mutableStateOf(false) }
    var tasks by remember { mutableStateOf(listOf<Tarea>()) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            // Tareas pendientes
            Text(
                text = "Tareas Pendientes",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            LazyColumn {
                items(tasks.filter { !it.completed }) { tarea ->
                    MyTaskCard(
                        tarea = tarea,
                        onCompleteTask = { updatedTask ->
                            tasks = tasks.map {
                                if (it == updatedTask) it.copy(completed = true) else it
                            }
                        },
                        onChangePriority = { updatedTask, newPriority ->
                            tasks = tasks.map {
                                if (it == updatedTask) it.copy(priority = newPriority) else it
                            }
                        }
                    )
                }
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Tareas completadas
            Text(
                text = "Tareas Completadas",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            LazyColumn {
                items(tasks.filter { it.completed }) { tarea ->
                    MyTaskCard(
                        tarea = tarea,
                        onCompleteTask = { updatedTask ->
                            tasks = tasks.map {
                                if (it == updatedTask) it.copy(completed = false) else it
                            }
                        },
                        onChangePriority = { updatedTask, newPriority ->
                            tasks = tasks.map {
                                if (it == updatedTask) it.copy(priority = newPriority) else it
                            }
                        }
                    )
                }
            }
        }

        FloatingActionButton(
            onClick = { showDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Añadir tarea")
        }
    }

    if (showDialog) {
        MyAlertDialog(
            onDismiss = { showDialog = false },
            onConfirm = { title, text, selectedPriority ->
                tasks = tasks + Tarea(title, text, selectedPriority, completed = false)
                showDialog = false
            }
        )
    }
}

@Composable
fun MyTaskCard(
    tarea: Tarea,
    onCompleteTask: (Tarea) -> Unit,
    onChangePriority: (Tarea, Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val priorityText = when (tarea.priority) {
        1 -> "Alta"
        2 -> "Media"
        3 -> "Baja"
        else -> "N/A"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(Color.LightGray) // Fondo claro para la tarjeta
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Título de la tarea
                Text(
                    tarea.title,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.weight(1f) // Asegura que el título ocupe el espacio disponible
                )

                // Usando BadgedBox para mostrar la prioridad
                BadgedBox(
                    modifier = Modifier
                        .padding(start = 8.dp) // Agregar padding para el badge
                        .wrapContentWidth(Alignment.End), // Alinea el badge a la derecha
                    badge = {
                        Text(
                            priorityText,
                            color = Color.White,
                            modifier = Modifier
                                .background(
                                    color = when (tarea.priority) {
                                        1 -> Color.Red
                                        2 -> Color.Yellow
                                        3 -> Color.Green
                                        else -> Color.Gray
                                    },
                                    shape = RoundedCornerShape(12.dp) // Forma redondeada del badge
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                ) {
                    // Espacio vacío necesario para el BadgedBox
                }
            }

            // Descripción de la tarea
            Text(tarea.text, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 8.dp))

            IconButton(onClick = { expanded = true }, modifier = Modifier.align(Alignment.End)) {
                Icon(Icons.Default.MoreVert, contentDescription = "Opciones")
            }

            // Menú desplegable para marcar como completada o cambiar prioridad
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                DropdownMenuItem(
                    text = { Text(if (tarea.completed) "Marcar como pendiente" else "Marcar como completada") },
                    onClick = {
                        expanded = false
                        onCompleteTask(tarea)
                    }
                )
                DropdownMenuItem(
                    text = { Text("Cambiar prioridad") },
                    onClick = {
                        expanded = false
                        // Cambiar prioridad (alta, media, baja en orden cíclico)
                        val newPriority = if (tarea.priority == 3) 1 else tarea.priority + 1
                        onChangePriority(tarea, newPriority)
                    }
                )
            }
        }
    }
}

data class Tarea(
    val title: String,
    val text: String,
    val priority: Int,
    val completed: Boolean = false
)

@Composable
fun MyAlertDialog(onDismiss: () -> Unit, onConfirm: (String, String, Int) -> Unit) {
    var title by remember { mutableStateOf("") }
    var text by remember { mutableStateOf("") }
    var selectedPriority by remember { mutableStateOf(2) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nueva Tarea") },
        text = {
            Column {
                TextField(value = title, onValueChange = { title = it }, label = { Text("Título") })
                Spacer(modifier = Modifier.height(8.dp))
                TextField(value = text, onValueChange = { text = it }, label = { Text("Descripción") })
                Spacer(modifier = Modifier.height(8.dp))
                Text("Prioridad")
                Row {
                    Button(onClick = { selectedPriority = 1 }) { Text("Alta") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { selectedPriority = 2 }) { Text("Media") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { selectedPriority = 3 }) { Text("Baja") }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                onConfirm(title, text, selectedPriority)
                title = ""
                text = ""
            }) { Text("Confirmar") }
        },
        dismissButton = {
            Button(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}
