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
        Text(text = "FullName: ${listItem.FullName}")
        Text(text = "Email: ${listItem.Email}")
        Text(text = "Phone: ${listItem.Phone}")
        Text(text = "Address: ${listItem.Address}")
        Text(text = "Photo: ${listItem.Photo}")
        Text(text = "About: ${listItem.About}")
        Text(text = "Birthday: ${listItem.Birthday}")
        Text(text = "Auth0UserId: ${listItem.Auth0UserId}")
        Text(text = "Id: ${listItem.Id}")
        Text(text = "LastEditedTime: ${listItem.LastEditedTime}")
        Text(text = "LastEditingUser: ${listItem.LastEditingUser}")
        Text(text = "LastEditingUserId: ${listItem.LastEditingUserId}")

        Spacer(modifier = androidx.compose.ui.Modifier.weight(2f, true))

        Button(onClick = { onBack() }) {
            Text(text = "Back")
        }
    }
}

