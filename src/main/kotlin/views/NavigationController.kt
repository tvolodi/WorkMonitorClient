package views

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable

class NavigationController(
    private val startDestination: String,
    private var backStackScreens: MutableSet<String> = mutableSetOf()
) {
    var currentScreen: MutableState<String> = mutableStateOf(startDestination)

    fun navigate(route: String) {
        if(route != currentScreen.value) {
            if (backStackScreens.contains(currentScreen.value) && currentScreen.value != startDestination) {
                backStackScreens.remove(currentScreen.value)
            }

            if (route == startDestination) {
                backStackScreens = mutableSetOf()
            } else {
                backStackScreens.add(currentScreen.value)
            }
            currentScreen.value = route
        }
    }

    fun navigateBack() {
        if (backStackScreens.isNotEmpty()) {
            currentScreen.value = backStackScreens.last()
            backStackScreens.remove(currentScreen.value)
        }
    }
}

@Composable
fun rememberNavigationController(
    startDestination: String,
    backStackScreens: MutableSet<String> = mutableSetOf()
): MutableState<NavigationController> = rememberSaveable{
    mutableStateOf(NavigationController(startDestination, backStackScreens))
}