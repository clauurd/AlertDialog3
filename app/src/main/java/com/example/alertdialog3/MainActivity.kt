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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Badge
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.alertdialog3.ui.theme.AlertDialog3Theme
import java.time.format.TextStyle

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            AlertDialog3Theme {
                var showDialog by remember { mutableStateOf(false) }
                var notesList by remember { mutableStateOf(listOf<Tarea>()) }
                Box(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        LazyColumn {
                            items(notesList) { tarea ->
                                MyCard(titulo = tarea.title, tarea = tarea.text, priority = tarea.priority)
                            }
                        }
                    }

                    // Botón flotante en la parte inferior derecha
                    FloatingActionButton(
                        onClick = { showDialog = true },
                        modifier = Modifier
                            .align(Alignment.BottomEnd) // Alineado en la esquina inferior derecha
                            .padding(16.dp) // Añadir algo de espacio alrededor del botón
                            .navigationBarsPadding()
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Añadir tarea")
                    }
                }

                if (showDialog) {
                    MyAlertDialog(
                        onDismiss = { showDialog = false },
                        onConfirm = { title, text,selectedPriority  ->
                            // Agregar una nueva nota a la lista
                            notesList = notesList + Tarea(title, text,selectedPriority)
                            showDialog = false
                        }
                    )
                }





            }
        }
}
@Composable
fun MyText(text:String,color: Color,style: FontStyle,tamanno: Int ){
    Text(text =text, color = color,fontStyle = style, fontSize = tamanno.sp )
}





    @Composable
    fun MyCard(titulo: String, tarea: String, priority: Int) {
        // El texto que se mostrará en el BadgeBox
        val priorityText = when (priority) {
            1 -> "Alta"
            2 -> "Media"
            3 -> "Baja"
            else -> "N/A"
        }

        // Card con BadgeBox
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    MyText(titulo, Color.Black, FontStyle.Normal, 15)

                    // BadgeBox con el color correspondiente
                    Badge(
                        content = {
                            Text(text = priorityText, color = Color.White) // Mostrar texto de la prioridad
                        },
                        modifier = Modifier
                            .padding(8.dp)
                             // Cambiar color del BadgeBox según la prioridad
                    )
                }

                MyText(tarea, Color.Black, FontStyle.Italic, 10)
            }
        }
    }}
data class Tarea(
    val title: String,
    val text: String,
    val priority: Int
)
    @Composable
    fun MyAlertDialog(onDismiss: () -> Unit, onConfirm: (String, String, Int) -> Unit) {
        var title by remember { mutableStateOf("") }
        var text by remember { mutableStateOf("") }
        var expanded by remember { mutableStateOf(false) } // Controla la visibilidad del DropdownMenu
        var selectedPriority by remember { mutableStateOf(2) } // Valor inicial de prioridad: Media (2)
        var priorityLabel by remember { mutableStateOf("Media") } // Etiqueta inicial del botón

        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(text = "Agregar Información")
            },
            text = {
                Column {
                    TextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Título") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = text,
                        onValueChange = { text = it },
                        label = { Text("Texto") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // DropdownMenu para seleccionar la prioridad
                    Box {
                        Button(onClick = { expanded = true }) {
                            Text("Prioridad: $priorityLabel")
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Alta") },
                                onClick = {
                                    selectedPriority = 1
                                    priorityLabel = "Alta"
                                    expanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Media") },
                                onClick = {
                                    selectedPriority = 2
                                    priorityLabel = "Media"
                                    expanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Baja") },
                                onClick = {
                                    selectedPriority = 3
                                    priorityLabel = "Baja"
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    onConfirm(title, text, selectedPriority)
                    title = "" // Limpiar el campo de título
                    text = ""  // Limpiar el campo de texto
                }) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                Button(onClick = onDismiss) {
                    Text("Cancelar")
                }
            }
        )
    }



