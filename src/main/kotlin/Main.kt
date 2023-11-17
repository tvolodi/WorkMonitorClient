import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.sharp.Home
import androidx.compose.material.icons.sharp.Info
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import auth.AuthManager
import kotlinx.coroutines.launch
import utils.GlobalConfig
import utils.runScheduledScreenshot
import utils.takeScreenshot

var mainWindow: FrameWindowScope? = null

val domain = "dev-e3r1z7qh4iztrv5x.us.auth0.com"
val clientId = "LKi23IJ0wTFZ3aAV0dwQUyRvFKdsA4zW"
val redirectUri = "http://localhost:5789/callback"
val scope = "openid offline_access"
val audience = "https://www.vt-ptm.org/wm-api"

@Composable
@Preview
fun App() {

    var text by remember { mutableStateOf("Hello, World!") }
    var currDrawerState = rememberDrawerState(DrawerValue.Closed)
    var isMenuExpanded by remember { mutableStateOf(false) }
    var isAccountMenuExpanded by remember { mutableStateOf(false) }
    val authManager = remember { AuthManager }

    val scaffoldState = rememberScaffoldState()
    val appScope = rememberCoroutineScope()

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
            drawerGesturesEnabled = true,
            drawerContent = {
                Column {
                    Text("Item 1")
                    Text("Item 2")
                    Text("Item 3")
                }
            }


        ) { innerPadding ->
            Column(

            ) {
                Text(text = "Primary: $text", color = MaterialTheme.colorScheme.primary)

                Button(onClick = {
                    text = "Hello, Desktop!"
                }) {
                    Text("Click me!")
                }
            }
        }
    }
}

fun main() = application {

    // Restore configuration from file
    GlobalConfig.restoreAuthConfig()

    // Restore tokens from file
    AuthManager.restoreTokens()

    val isOpen = remember { mutableStateOf(true) }
    var windowScope: FrameWindowScope

//    val image: Image = BufferedImage()// Toolkit.getDefaultToolkit().getImage(url)

    if(isOpen.value) {
        Window(title = "Work Monitoring",
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

