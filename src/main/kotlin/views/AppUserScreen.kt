package views

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize

sealed class AppUserScreen: Parcelable {
    @Parcelize
    object List: AppUserScreen()
    @Parcelize
    data class Details(val listItem: entity_models.AppUser): AppUserScreen()
}