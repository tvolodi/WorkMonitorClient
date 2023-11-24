package views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import entity_models.AppUser

@Composable
fun AppUserDetails( listItem: AppUser, onBack: () -> Unit) {
    Column() {
        Text(text = "FullName: ${listItem.fullName}")
        Text(text = "Email: ${listItem.email}")
        Text(text = "Phone: ${listItem.phone}")
        Text(text = "Address: ${listItem.address}")
        Text(text = "Photo: ${listItem.photo}")
        Text(text = "About: ${listItem.about}")
        Text(text = "Birthday: ${listItem.birthday}")
        Text(text = "Auth0UserId: ${listItem.auth0UserId}")
        Text(text = "Id: ${listItem.id}")
        Text(text = "LastEditedTime: ${listItem.lastEditedTime}")
        Text(text = "LastEditingUser: ${listItem.lastEditingUser}")
        Text(text = "LastEditingUserId: ${listItem.lastEditingUserId}")

        Spacer(modifier = androidx.compose.ui.Modifier.weight(2f, true))

        Button(onClick = { onBack() }) {
            Text(text = "Back")
        }
    }
}

