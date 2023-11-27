package views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import entity_models.ProjectTask
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier

@Composable
fun ProjectTaskView() {
    val projectTask = remember { mutableStateOf(ProjectTask("", "")) }
//    val projectTaskList = remember { mutableStateOf(listOf<ProjectTask>()) }

    Column {

        TextField(
            value = projectTask.value.projectName,
            onValueChange = { projectTask.value.projectName = it },
            label = { Text("Project Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        TextField(
            value = projectTask.value.taskName,
            onValueChange = { projectTask.value.taskName = it },
            label = { Text("Task Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        Button(
            onClick = {

            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text("Update")
        }
    }
}
