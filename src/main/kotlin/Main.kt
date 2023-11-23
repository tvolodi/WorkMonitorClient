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
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.skia.paragraph.Alignment
import utils.GlobalConfig
import utils.runScheduledScreenshot
import utils.takeScreenshot
import views.*
import java.awt.SystemColor


var mainWindow: FrameWindowScope? = null

val domain = "dev-e3r1z7qh4iztrv5x.us.auth0.com"
val clientId = "LKi23IJ0wTFZ3aAV0dwQUyRvFKdsA4zW"
val redirectUri = "http://localhost:5789/callback"
val scope = "openid offline_access"
val audience = "https://www.vt-ptm.org/wm-api"

@Composable
@Preview
fun App() {

    val screenViews = ScreenViews.entries
    val navigationController by rememberNavigationController(ScreenViews.HOME_SCREEN.name)
    val currentScreen by remember {
        navigationController.currentScreen
    }
    var selectedSreenView = remember { mutableStateOf(ScreenViews.HOME_SCREEN) }

    val navigation = remember {StackNavigation<AppChilds>()}

    var text by remember { mutableStateOf("Hello, World!") }
    var currDrawerState = rememberDrawerState(DrawerValue.Closed)
    var isMenuExpanded by remember { mutableStateOf(false) }
    var isAccountMenuExpanded by remember { mutableStateOf(false) }
    val authManager = remember { AuthManager }

    val scaffoldState = rememberScaffoldState()
    val appScope = rememberCoroutineScope()

    var selectedView  = remember { mutableStateOf(MainViews.USER_VIEW) }

    var isAccessTokenExists = remember { mutableStateOf(GlobalConfig.areTokenExist)}

    var hostText = remember{mutableStateOf(GlobalConfig.appConfig["app_server_host"] ?: "")}
    var portText = remember{mutableStateOf(GlobalConfig.appConfig["app_server_port"] ?: "")}
    var protocolText = remember{mutableStateOf(GlobalConfig.appConfig["app_server_protocol"] ?: "")}

    var authDomainText = remember{mutableStateOf(GlobalConfig.authConfig["auth_domain"] ?: "")}
    var authClientIdText = remember{mutableStateOf(GlobalConfig.authConfig["auth_client_id"] ?: "")}
    var authRedirectUriText = remember{mutableStateOf(GlobalConfig.authConfig["auth_redirect_uri"] ?: "")}
    var authAudienceText = remember{mutableStateOf(GlobalConfig.authConfig["auth_audience"] ?: "")}


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
                            enabled = isAccessTokenExists.value,
                            onClick = {
                                appScope.launch {
                                    // selectedView.value = MainViews.USER_VIEW
                                }
    //                            selectedSreenView.value = ScreenViews.APPUSERS_SCREEN
                            navigationController.navigate(ScreenViews.APPUSERS_SCREEN.name)
                            }
                        ) {
                            Icon(Icons.Filled.Person, contentDescription = null)
                        }

                        // Project View
                        IconButton(
                            enabled = isAccessTokenExists.value,
                            onClick = {
                                selectedView.value = MainViews.PROJECT_VIEW
                                navigationController.navigate(ScreenViews.PROJECTS_SCREEN.name)
                            }
                        ) {
                            Icon(Icons.Filled.ShoppingCart, contentDescription = null)
                        }

                        // Tasks View
                        IconButton(
//                            enabled = isAccessTokenExists.value,
                            onClick = {
//                                selectedView.value = MainViews.PROJECT_VIEW
//                                navigationController.navigate(ScreenViews.PROJECTS_SCREEN.name)
                                GlobalConfig.areTokenExist = true
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
//            drawerGesturesEnabled = true,
//            drawerContent = {
//                Column {
//                    Text("Item 1")
//                    Text("Item 2")
//                    Text("Item 3")
//                }
//            }


        ) { innerPadding ->

            Column(modifier = Modifier.width(IntrinsicSize.Max)) {

                // Application server host
//                Text(text = "Host name")
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
                    value = protocolText .value,
                    onValueChange = { protocolText.value = it },
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
                        onClick = {appScope.launch {} },
                        modifier = Modifier.border(2.dp, MaterialTheme.colorScheme.onPrimary)
                            .padding(4.dp),
//                    .shadow(elevation = 3.dp, shape = MaterialTheme.shapes.small,
                    ) {
                        Icon(Icons.Sharp.Done, contentDescription = null)
                    }
                }

            }


//            screenViews.forEach{
//                if (it == selectedSreenView.value) {
//                    navigationController.navigate(it.name)
//                }
//            }

//             customNavigationHost(navigationController = navigationController)

//            when (selectedView.value) {
//                MainViews.USER_VIEW -> {
//                    AppUserList()
//                }
//
//                MainViews.PROJECT_VIEW -> {
//                    ProjectList()
//                }
//
//                else -> {
//                    Text("Hello, World!")}
//            }

//            ChildStack(
//                source = navigation,
//                initialStack = { listOf(AppUserView) },
//                handleBackButton = true,
//                animation = stackAnimation(fade() + scale()),
//            ) { child ->
//                when (child) {
//                    is AppUserView -> {
//                        AppUserList()
//                    }
//
//                    is ProjectView -> {
//                        ProjectList()
//                    }
//                }
//            }
        }
    }
}

fun main(){

    val lifecycle = LifecycleRegistry()

    val rootComponentContext = DefaultComponentContext(lifecycle = lifecycle)

    application {

        val windowState = rememberWindowState(WindowPlacement.Maximized)
        LifecycleController(lifecycle, windowState)

        // Restore configuration from file
        GlobalConfig.authConfig = GlobalConfig.readConfig("wm_auth_config.json")
        GlobalConfig.appConfig = GlobalConfig.readConfig("wm_app_config.json")
        GlobalConfig.tokens = GlobalConfig.readConfig("wm_tokens.json")
        if(GlobalConfig.tokens.isEmpty()){
            GlobalScope.launch {
                AuthManager.authenticateUser()
            }
        }

        val isOpen = remember { mutableStateOf(true) }
        var windowScope: FrameWindowScope

//    val image: Image = BufferedImage()// Toolkit.getDefaultToolkit().getImage(url)

        if(isOpen.value) {
            Window(
                title = "Work Monitoring",
                state = windowState,
                onCloseRequest = {
                    isOpen.value = false
                }) { //  ::exitApplication
                mainWindow = this

                CompositionLocalProvider(LocalComponentContext provides rootComponentContext) {
                    ProvideComponentContext(rootComponentContext) {
                        App()
                    }
                }
//                App()
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
    APPUSERS_SCREEN(
        label = "App Users",
        icon = Icons.Filled.Person
    ),
    PROJECTS_SCREEN(
        label = "Projects",
        icon = Icons.Filled.ShoppingCart
    ),
    NOTIFICATIONS_SCREEN(
        label = "Notifications",
        icon = Icons.Filled.Notifications
    ),
    SETTINGS_SCREEN(
        label = "Settings",
        icon = Icons.Filled.Settings
    ),
    PROFILE_SCREEN(
        label = "User Profile",
        icon = Icons.Filled.AccountCircle
    )
}

@Composable
fun customNavigationHost(
    navigationController: NavigationController
) {
    NavigationHost(navigationController) {
        composable(ScreenViews.HOME_SCREEN.name) {
            Text("Home Screen")
        }

        composable(ScreenViews.APPUSERS_SCREEN.name) {
            AppUserList()
        }

        composable(ScreenViews.PROJECTS_SCREEN.name) {
            ProjectList()
        }

        composable(ScreenViews.NOTIFICATIONS_SCREEN.name) {
            Text("Notifications Screen")
        }

        composable(ScreenViews.SETTINGS_SCREEN.name) {
            Text("Settings Screen")
        }

        composable(ScreenViews.PROFILE_SCREEN.name) {
            Text("Profile Screen")
        }
    }.build()
}

