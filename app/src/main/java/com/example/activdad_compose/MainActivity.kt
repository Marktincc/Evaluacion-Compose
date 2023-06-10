package com.example.activdad_compose




import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.activdad_compose.ui.theme.MaterialTheme
import com.example.activdad_compose.ui.theme.*


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val todoList = remember {
                mutableStateListOf(
                    TodoItemData("Tarea 1", false),
                    TodoItemData("Tarea 2", false),
                    TodoItemData("Tarea 3", false)
                )
            }
            val newTaskText = remember { mutableStateOf("") }

            MaterialTheme {
                TodoScreen(todoList, newTaskText)
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen(todoList: MutableList<TodoItemData>, newTaskText: MutableState<String>) {
    Column(modifier = Modifier.fillMaxSize()) {


        Row(
            modifier = Modifier
                .height(70.dp)
                .fillMaxWidth()


                .background(MaterialTheme.colorScheme.primary),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Listado de tareas",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.surface
            )
        }

        TextField(
            value = newTaskText.value,
            onValueChange = { newTaskText.value = it },
            label = { Text(text = "Nueva tarea") },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        )
        AddRemoveButtons(todoList, newTaskText)

        TodoList(todoList)
    }
}


@Composable
fun TodoList(todoList: MutableList<TodoItemData>) {
    Column() {
        Box(modifier = Modifier.weight(1f)) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(todoList) { todo ->
                    TodoItem(todo = todo) {
                        todoList.remove(todo)
                    }
                }
            }
        }
    }

}



@Composable
fun AddRemoveButtons(todoList: MutableList<TodoItemData>, newTaskText: MutableState<String>) {
    val showDialog = remember { mutableStateOf(false) }
    Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.Center) {
        Button(
            onClick = {
                val newTask = newTaskText.value.trim()
                if (newTask.isNotEmpty()) {
                    todoList.add(TodoItemData(newTask, false))
                    newTaskText.value = ""
                }
            },
            modifier = Modifier
                .height(50.dp)
                .width(200.dp),
            colors = ButtonDefaults.buttonColors()
        ) {
            Text(text = "Agregar tarea", color = MaterialTheme.colorScheme.onSurface)
        }

        Spacer(modifier = Modifier.width(16.dp))

        Button(
            onClick = { showDialog.value = true },
            modifier = Modifier
                .height(50.dp)
                .width(200.dp),
            colors = ButtonDefaults.buttonColors()
        ) {
            Text(text = "Eliminar tareas", color = MaterialTheme.colorScheme.surface)
        }
    }

    if (showDialog.value) {
        AlertDialog(

            onDismissRequest = { showDialog.value = false },
            title = { Text(text = "Eliminar tareas realizadas",color = MaterialTheme.colorScheme.primary) },
            text = { Text(text = "¿Estás seguro de que deseas eliminar las tareas relaizadas?",color = MaterialTheme.colorScheme.primary) },
            confirmButton = {
                Button(
                    onClick = {
                        todoList.removeAll { it.checked }
                        showDialog.value = false
                    }
                ) {
                    Text(text = "Eliminar",color = MaterialTheme.colorScheme.surface)
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog.value = false }
                ) {
                    Text(text = "Cancelar",color = MaterialTheme.colorScheme.surface)
                }
            },

        )
    }

    }

@Composable
fun TodoItem(todo: TodoItemData, onTaskRemoved: () -> Unit) {
    var checkedState by remember { mutableStateOf(todo.checked) }

    LaunchedEffect(todo.checked) {
        checkedState = todo.checked
    }

    Row(modifier = Modifier.padding(16.dp)) {
        Checkbox(
            checked = checkedState,
            onCheckedChange = { isChecked ->
                checkedState = isChecked
                todo.checked = isChecked
            },
            modifier = Modifier.padding(end = 16.dp)
        )
        Text(text = todo.text)
    }
}

data class TodoItemData(
    val text: String,
    var checked: Boolean
)

@Composable
fun MyApplicationTheme(content: @Composable () -> Unit) {
    MaterialTheme {
        Surface(color = MaterialTheme.colorScheme.primary) {
            content()
        }
    }
}
