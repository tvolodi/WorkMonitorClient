import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.AccountCircle
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.window.*
import java.awt.*
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

var mainWindow: FrameWindowScope? = null

@Composable
@Preview
fun App() {
    var text by remember { mutableStateOf("Hello, World!") }

    MaterialTheme {
        Button(onClick = {

            var width = 0
            var height = 0
            val ge = GraphicsEnvironment.getLocalGraphicsEnvironment()
            val gDevices = ge.screenDevices
            for(dev in gDevices){
                val displayMode = dev.displayMode
                width += displayMode.width
                height = displayMode.height
            }

            val screenRectangle = Rectangle(0, 0, width, height)
            val captureImage = Robot().createScreenCapture(screenRectangle)
            ImageIO.write(captureImage, "png", File("ScreenShot.png"))

            text = "Hello, Desktop!"
        }) {
            Text(text)
        }
    }
}

fun main() = application {
    val isOpen = remember { mutableStateOf(true) }
    var windowScope: FrameWindowScope
    val url = "Flat-Icons.com-Flat-Clock.16.png"

//    val image: Image = BufferedImage()// Toolkit.getDefaultToolkit().getImage(url)

    if(isOpen.value) {
        Window(onCloseRequest = {
            isOpen.value = false
        }) { //  ::exitApplication
            mainWindow = this

            App()
        }
    }

//    createTray(this, isOpen)

}

fun createTray(app: ApplicationScope, isMainWindowOpen: MutableState<Boolean>): Unit {

    var tray = SystemTray.getSystemTray()
    val popup: PopupMenu =  PopupMenu()
    // val url = System::class.java.getResource("/images/new.png")
    val url = "Flat-Icons.com-Flat-Clock.16.png"
    val image: Image = Toolkit.getDefaultToolkit().getImage(url)
    val trayIcon = TrayIcon(image)
    val aboutItem = MenuItem("About")

    val displayItem = MenuItem("Display")
    displayItem.addActionListener {
        isMainWindowOpen.value = true
    }

    val takeScreenItem = MenuItem("Take Screenshot")
    val exitItem = MenuItem("Exit")
    exitItem.addActionListener {
        app.exitApplication()
    }

    popup.add(aboutItem)
    popup.add(displayItem)
    popup.add(takeScreenItem)
    popup.add(exitItem)
    trayIcon.setPopupMenu(popup)

    try {
        tray.add(trayIcon)
    } catch (e: AWTException) {
        println("TrayIcon could not be added.")
    }

}
