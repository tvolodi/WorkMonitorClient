package views

import androidx.compose.runtime.Composable

class NavigationHost(
    val navigationController: NavigationController,
    val contents: @Composable NavigationGraphBuilder.() -> Unit
) {

    inner class NavigationGraphBuilder(
        val navigationController: NavigationController = this@NavigationHost.navigationController){
        @Composable
        fun renderContents() {
            this@NavigationHost.contents(this)
        }

    }

    @Composable
    fun build() {
        NavigationGraphBuilder().renderContents()
    }


}

@Composable
fun NavigationHost.NavigationGraphBuilder.composable(
    route: String,
    content: @Composable () -> Unit
) {
    if(navigationController.currentScreen.value == route) {
        content()
    }

}