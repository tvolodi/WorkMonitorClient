package views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.dp
import entity_models.AppUser
import kotlinx.coroutines.launch
import services.GetRequest
import services.getAppUserList

@Composable
fun AppUserList() {

    val coroutineScope = rememberCoroutineScope()

    var listItems = remember { mutableStateListOf<AppUser>() }

    // Get AppUser list from API
    coroutineScope.launch {
        val appUserList = getAppUserList(GetRequest(null, null, null, null))
        listItems.clear()
        listItems.addAll(appUserList)
    }

    LazyColumn {
        items(listItems.size) { index ->
            Text(
                text = listItems[index].fullName,
                modifier = androidx.compose.ui.Modifier
                    .clickable {  }
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }


    // Text(text = "AppUserList")
}

//@Composable
//fun AppUserList(listItems: List<AppUser>, onItemClicked: (AppUser) -> Unit) {
//
//    val items = remember { listItems }
//
//    LazyColumn {
//        items(100) { index ->
//            Text(
//                text = items[index].FullName,
//                modifier = androidx.compose.ui.Modifier
//                    .clickable { onItemClicked(items[index])}
//                    .fillMaxWidth()
//                    .padding(16.dp)
//            )
//        }
//    }
//
//}