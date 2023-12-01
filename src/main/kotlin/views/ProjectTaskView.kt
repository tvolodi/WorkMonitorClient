package views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.Button
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import services.readConfigValue
import services.saveConfigValue

@Composable
fun ProjectTaskView() {

    var projectName = remember { mutableStateOf(readConfigValue("project_name")) }
    var taskName = remember { mutableStateOf(readConfigValue("task_name")) }
    val routineScope = rememberCoroutineScope()

//    val projectTaskList = remember { mutableStateOf(listOf<ProjectTask>()) }

    Column {

        TextField(
            value = projectName.value,
            onValueChange = { projectName.value = it },
            label = { Text("Project Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        TextField(
            value = taskName.value,
            onValueChange = { taskName.value = it },
            label = { Text("Task Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        Button(
            onClick = {
                routineScope.launch {
                    saveConfigValue("project_name", projectName.value)
                    saveConfigValue("task_name", taskName.value)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text("Update")
        }
    }
}
