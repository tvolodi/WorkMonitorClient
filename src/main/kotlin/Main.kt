import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.sharp.AccountCircle
import androidx.compose.material.icons.sharp.Home
import androidx.compose.material.icons.sharp.Info
import androidx.compose.material.icons.sharp.List
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.*
import java.awt.*
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp

var mainWindow: FrameWindowScope? = null

@Composable
@Preview
fun App() {

    var text by remember { mutableStateOf("Hello, World!") }
    var drawerState = rememberDrawerState(DrawerValue.Closed)
    var isMenuExpanded by remember { mutableStateOf(false) }
    var isMenuExpanded2 by remember { mutableStateOf(false) }

    MaterialTheme (colors = lightColors(),
        typography = Typography(defaultFontFamily = FontFamily.SansSerif),
        shapes = Shapes(small = MaterialTheme.shapes.small, medium = MaterialTheme.shapes.medium, large = MaterialTheme.shapes.large)
    ) {
        Scaffold (
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
                        IconButton(onClick = { isMenuExpanded2 = true }) {
                            Icon(Icons.Sharp.List, contentDescription = null)
                        }

                        DropdownMenu(
                            expanded = isMenuExpanded2,
                            onDismissRequest = { isMenuExpanded2 = false },
                        ) {
                            DropdownMenuItem(onClick = { /* Handle refresh! */ }) {
                                Text("Refresh")
                            }
                            DropdownMenuItem(onClick = { /* Handle settings! */ }) {
                                Text("Settings")
                            }
                            Divider()
                            DropdownMenuItem(onClick = { /* Handle send feedback! */ }) {
                                Text("Send Feedback")
                            }
                            DropdownMenuItem(onClick = { /* Handle send feedback! */ }) {
                                Text("Send Feedback")
                            }
                        }

                        IconButton(onClick = { /* doSomething() */ }) {
                            Icon(Icons.Sharp.AccountCircle, contentDescription = null)
                        }
                        IconButton(onClick = { /* doSomething() */ }) {
                            Icon(Icons.Sharp.AccountCircle, contentDescription = null)
                        }
                        IconButton(onClick = { /* doSomething() */ }) {
                            Icon(Icons.Sharp.AccountCircle, contentDescription = null)
                        }
                        IconButton(onClick = { /* doSomething() */ }) {
                            Icon(Icons.Sharp.AccountCircle, contentDescription = null)
                        }
                        IconButton(onClick = { /* doSomething() */ }) {
                            Icon(Icons.Sharp.AccountCircle, contentDescription = null)
                        }

                        IconButton(onClick = {
                            isMenuExpanded = true
                        }) {
                            Icon(Icons.Filled.Menu, contentDescription = null)
                            DropdownMenu(
                                expanded = isMenuExpanded,
                                onDismissRequest = { isMenuExpanded = false },
//                            modifier = Modifier
//                                .border(1.dp, MaterialTheme.colorScheme.onPrimary)
//                                .padding(1.dp)
//                                .shadow(elevation = 2.dp, shape = MaterialTheme.shapes.small),
                            ) {
                                DropdownMenuItem(onClick = { /* Handle refresh! */ }) {
                                    Text("Refresh")
                                }
                                DropdownMenuItem(onClick = { /* Handle settings! */ }) {
                                    Text("Settings")
                                }
                                Divider()
                                DropdownMenuItem(onClick = { /* Handle send feedback! */ }) {
                                    Text("Send Feedback")
                                }
                            }
                        }



                        IconButton(onClick = { /* doSomething() */ }) {
                            Icon(Icons.Sharp.AccountCircle, contentDescription = null)
                        }
                        IconButton(onClick = { /* doSomething() */ }) {
                            Icon(Icons.Sharp.AccountCircle, contentDescription = null)
                        }
                        IconButton(onClick = { /* doSomething() */ }) {
                            Icon(Icons.Sharp.AccountCircle, contentDescription = null)
                        }

                        IconButton(onClick = { /* doSomething() */ }) {
                            Icon(Icons.Sharp.AccountCircle, contentDescription = null)
                        }
                        IconButton(onClick = { /* doSomething() */ }) {
                            Icon(Icons.Sharp.AccountCircle, contentDescription = null)
                        }
                        IconButton(onClick = { /* doSomething() */ }) {
                            Icon(Icons.Sharp.AccountCircle, contentDescription = null)
                        }
                        IconButton(onClick = { /* doSomething() */ }) {
                            Icon(Icons.Sharp.AccountCircle, contentDescription = null)
                        }

                    },
                    navigationIcon = {
                        IconButton(onClick = {
//                            drawerState.open()
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
                Text(text = "Inverse: $text", color = MaterialTheme.colorScheme.inversePrimary)
                Text(text = "OnPrimary: $text", color = MaterialTheme.colorScheme.onPrimary)
                Text(text = "Secondary: $text", color = MaterialTheme.colorScheme.secondary)
//                Text(text = "Inverse: $text", color = MaterialTheme.colorScheme.inverseSecondary)
                Text(text = "Background: $text", color = MaterialTheme.colorScheme.background)
                Text(text = "Outline: $text", color = MaterialTheme.colorScheme.outline)
                Text(text = "Outline: $text", color = MaterialTheme.colorScheme.onPrimary)
                Text(text = "Error: $text", color = MaterialTheme.colorScheme.error)
                Text(text = "Surface: $text", color = MaterialTheme.colorScheme.surface)
                Text(text = "Inverse: $text", color = MaterialTheme.colorScheme.inverseSurface)

                Button(onClick = {
                    text = "Hello, Desktop!"
                }) {
                    Text("Click me!")
                }
            }
        }
    }

//    MaterialTheme {
//        Button(onClick = {
//
//            var width = 0
//            var height = 0
//            val ge = GraphicsEnvironment.getLocalGraphicsEnvironment()
//            val gDevices = ge.screenDevices
//            for(dev in gDevices){
//                val displayMode = dev.displayMode
//                width += displayMode.width
//                height = displayMode.height
//            }
//
//            val screenRectangle = Rectangle(0, 0, width, height)
//            val captureImage = Robot().createScreenCapture(screenRectangle)
//            ImageIO.write(captureImage, "png", File("ScreenShot.png"))
//
//            text = "Hello, Desktop!"
//        }) {
//            Text(text)
//        }
//    }
}

fun main() = application {
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
}

//fun createTray(app: ApplicationScope, isMainWindowOpen: MutableState<Boolean>): Unit {
//
//    var tray = SystemTray.getSystemTray()
//    val popup: PopupMenu =  PopupMenu()
//    // val url = System::class.java.getResource("/images/new.png")
//    val url = "Flat-Icons.com-Flat-Clock.16.png"
//    val image: Image = Toolkit.getDefaultToolkit().getImage(url)
//    val trayIcon = TrayIcon(image)
//    val aboutItem = MenuItem("About")
//
//    val displayItem = MenuItem("Display")
//    displayItem.addActionListener {
//        isMainWindowOpen.value = true
//    }
//
//    val takeScreenItem = MenuItem("Take Screenshot")
//    val exitItem = MenuItem("Exit")
//    exitItem.addActionListener {
//        app.exitApplication()
//    }
//
//    popup.add(aboutItem)
//    popup.add(displayItem)
//    popup.add(takeScreenItem)
//    popup.add(exitItem)
//    trayIcon.setPopupMenu(popup)
//
//    try {
//        tray.add(trayIcon)
//    } catch (e: AWTException) {
//        println("TrayIcon could not be added.")
//    }
//
//}
