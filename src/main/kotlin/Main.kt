import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import java.awt.DisplayMode
import java.awt.GraphicsEnvironment
import java.awt.Rectangle
import java.awt.Robot
import java.io.File
import javax.imageio.ImageIO

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
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
