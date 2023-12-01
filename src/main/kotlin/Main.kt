import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.sharp.Done
import androidx.compose.material.icons.sharp.Home
import androidx.compose.material.icons.sharp.Info
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import auth.AuthManager
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.lifecycle.LifecycleController
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import services.checkAndCreateDatabase
import services.readConfigValue
import services.saveConfigValue
import utils.*
import views.*


var mainWindow: FrameWindowScope? = null

//val domain = "dev-e3r1z7qh4iztrv5x.us.auth0.com"
//val clientId = "LKi23IJ0wTFZ3aAV0dwQUyRvFKdsA4zW"
//val redirectUri = "http://localhost:5789/callback"
//val scope = "openid offline_access"
//val audience = "https://www.vt-ptm.org/wm-api"

@Composable
@Preview
fun App() {

    var selectedSreenView = remember { mutableStateOf(ScreenViews.HOME_SCREEN) }

    var isAccountMenuExpanded by remember { mutableStateOf(false) }
    val authManager = remember { AuthManager }

    val scaffoldState = rememberScaffoldState()
    val appScope = rememberCoroutineScope()

    var selectedView  = remember { mutableStateOf(MainViews.USER_VIEW) }

    var isAccessTokenExists = remember { mutableStateOf(true) }  // { mutableStateOf(GlobalConfig.areTokenExist)}

    MaterialTheme (colors = lightColors(),
        typography = Typography(defaultFontFamily = FontFamily.SansSerif),
        shapes = Shapes(small = MaterialTheme.shapes.small, medium = MaterialTheme.shapes.medium, large = MaterialTheme.shapes.large)
    ) {
        Scaffold (
            scaffoldState = scaffoldState,
            topBar = {
                TopAppBar(
                    title = { Text("Work Monitoring") },
                    elevation = 12.dp,
                    modifier = Modifier
                        .border(2.dp, MaterialTheme.colorScheme.onPrimary)
                        .padding(4.dp)
//                        .height(40.dp)
                        .shadow(elevation = 3.dp, shape = MaterialTheme.shapes.small),
                    actions = {

                        // AppUsers View
                        IconButton(
//                            enabled = isAccessTokenExists.value,
                            onClick = {
                                selectedSreenView.value = ScreenViews.SETTINGS_SCREEN
                            }
                        ) {
                            Icon(Icons.Filled.Settings, contentDescription = null)
                        }

                        // Project View
                        IconButton(
                            enabled = isAccessTokenExists.value,
                            onClick = {
                                selectedView.value = MainViews.PROJECT_VIEW
                            }
                        ) {
                            Icon(Icons.Filled.ShoppingCart, contentDescription = null)
                        }

                        // Tasks View
                        IconButton(
//                            enabled = isAccessTokenExists.value,
                            onClick = {
                                selectedSreenView.value = ScreenViews.HOME_SCREEN
                            }
                        ) {
                            Icon(Icons.Filled.List, contentDescription = null)
                        }

                        IconButton(onClick = {
                            appScope.launch {
                                takeScreenshot()
                            }
                        }) {
                            Icon (painterResource("ScreenshotIcon.svg"), contentDescription = null, modifier = Modifier.padding(8.dp))
                        }

                        IconButton(onClick = { isAccountMenuExpanded = true }) {
                            Icon(Icons.Filled.AccountCircle, contentDescription = null)
                        }

                        DropdownMenu(
                            expanded = isAccountMenuExpanded,
                            onDismissRequest = { isAccountMenuExpanded = false },
                        ) {
                            DropdownMenuItem(onClick = {
                                appScope.launch {
                                    authManager.authenticateUser()
                                }
                            }) {
                                Text("Login")
                            }
                            DropdownMenuItem(onClick = { /* Handle settings! */ }) {
                                Text("Logout")
                            }
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            appScope.launch { scaffoldState.drawerState.open() }
                        }) {
                            Icon(Icons.Sharp.Home, contentDescription = null)
                        }
                    }
                )
            },
            bottomBar = {
                BottomAppBar(
                    backgroundColor = MaterialTheme.colorScheme.outlineVariant,
                    modifier = Modifier
                        .border(2.dp, MaterialTheme.colorScheme.onPrimary)
                        .padding(4.dp)
                        .shadow(elevation = 3.dp, shape = MaterialTheme.shapes.small),
                ) {
                    IconButton(onClick = { /* doSomething() */ }) {
                        Icon(Icons.Sharp.Info, contentDescription = null)
                    }
                }
            },
        ) { innerPadding ->

            when(selectedSreenView.value){
                ScreenViews.HOME_SCREEN -> {
                    ProjectTaskView()
                }

                ScreenViews.SETTINGS_SCREEN -> {
                    ConfigView()
                }

                else -> {}
            }
        }
    }
}

@Composable
private fun ConfigView(
) {
    var hostText = remember{mutableStateOf( readConfigValue("app_server_host"))}
    var portText = remember{mutableStateOf(readConfigValue("app_server_port"))}
    var protocolText = remember{mutableStateOf(readConfigValue("app_server_protocol"))}

    var authDomainText = remember{mutableStateOf(readConfigValue("auth_domain"))}
    var authClientIdText = remember{mutableStateOf(readConfigValue("auth_client_id"))}
    var authRedirectUriText = remember{mutableStateOf(readConfigValue("auth_redirect_uri"))}
    var authAudienceText = remember{mutableStateOf(readConfigValue("auth_audience"))}

    val routineScope = rememberCoroutineScope()

    Column(modifier = Modifier.width(IntrinsicSize.Max)) {
        TextField(
            value = hostText.value,
            onValueChange = { hostText.value = it },
            label = { Text("Host Name") },
            modifier = Modifier.padding(4.dp).fillMaxWidth()
        )
        TextField(
            value = portText.value,
            onValueChange = { portText.value = it },
            label = { Text("Port Number") },
            modifier = Modifier.padding(4.dp).fillMaxWidth()
        )
        TextField(
            value = protocolText.value,
            onValueChange = {
                protocolText.value = it
            },
            label = { Text("Protocol (HTTP/HTTPS)") },
            modifier = Modifier.padding(4.dp).fillMaxWidth()
        )

        Divider(modifier = Modifier.padding(8.dp), thickness = 2.dp)
        TextField(
            value = authDomainText.value,
            onValueChange = { authDomainText.value = it },
            label = { Text("Authentication host") },
            modifier = Modifier.padding(4.dp).fillMaxWidth()
        )
        TextField(
            value = authClientIdText.value,
            onValueChange = { authClientIdText.value = it },
            label = { Text("Client Id") },
            modifier = Modifier.padding(4.dp).fillMaxWidth()
        )
        TextField(
            value = authRedirectUriText.value,
            onValueChange = { authRedirectUriText.value = it },
            label = { Text("Redirection URL") },
            modifier = Modifier.padding(4.dp).fillMaxWidth()
        )
        TextField(
            value = authAudienceText.value,
            onValueChange = { authAudienceText.value = it },
            label = { Text("Auth Audience") },
            modifier = Modifier.padding(4.dp).fillMaxWidth()
        )

        Divider(modifier = Modifier.padding(8.dp), thickness = 2.dp)
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = {
                    routineScope.launch {
                        saveConfigValue("app_server_protocol", protocolText.value)
                        saveConfigValue("app_server_host", hostText.value)
                        saveConfigValue("app_server_port", portText.value)
                        saveConfigValue("auth_domain", authDomainText.value)
                        saveConfigValue("auth_client_id", authClientIdText.value)
                        saveConfigValue("auth_redirect_uri", authRedirectUriText.value)
                        saveConfigValue("auth_audience", authAudienceText.value)
                    }
                },
                modifier = Modifier.border(2.dp, MaterialTheme.colorScheme.onPrimary)
                    .padding(4.dp),
//                    .shadow(elevation = 3.dp, shape = MaterialTheme.shapes.small,
            ) {
                Icon(Icons.Sharp.Done, contentDescription = null)
            }
        }

    }
}

fun main(){

    checkAndCreateDatabase()

    application {

        val windowState = rememberWindowState(WindowPlacement.Maximized)

        if(readConfigValue("accessToken") == ""){
            AuthManager.authenticateUser()
        }

        val isOpen = remember { mutableStateOf(true) }

        if(isOpen.value) {
            Window(
                title = "Work Monitoring",
                state = windowState,
                onCloseRequest = {
                    isOpen.value = false
                }) { //  ::exitApplication
                mainWindow = this
                App()
            }
        }

        if(isTraySupported) {
            Tray(
                icon = painterResource("Flat-Icons.com-Flat-Clock.16.png"),
                menu = {
                    Item("About", onClick = {

                    })
                    Item("Display", onClick = {
                        isOpen.value = true
                    })
                    Item("Take Screenshot", onClick = {

                    })
                    Item("Exit", onClick = {
                        exitApplication()
                    })
                }
            )
        }

        // Run a screenshot task every periodLength minutes
        runScheduledScreenshot(15)
    }
}

enum class ScreenViews(
    val label: String,
    val icon: ImageVector
){
    HOME_SCREEN(
        label = "Home",
        icon = Icons.Filled.Home
    ),
    PROJECTS_SCREEN(
        label = "Projects",
        icon = Icons.Filled.ShoppingCart
    ),
    SETTINGS_SCREEN(
        label = "Config",
        icon = Icons.Filled.Settings
    ),
}
