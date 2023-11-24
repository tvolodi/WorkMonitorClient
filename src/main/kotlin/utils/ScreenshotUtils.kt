package utils

import kotlinx.coroutines.*
import services.uploadFile
import java.awt.GraphicsEnvironment
import java.awt.Rectangle
import java.awt.Robot
import java.io.File
import java.time.LocalDateTime
import java.util.*
import javax.imageio.ImageIO

private class ScreenshotTask: TimerTask() {
    override fun run() {

        GlobalScope.launch {
            takeScreenshot()
        }

    }
}

suspend fun takeScreenshot(displayOrderMode : Int = 0){
    var width = 0
    var height = 0
    val ge = GraphicsEnvironment.getLocalGraphicsEnvironment()
    val gDevices = ge.screenDevices

    val  firstDisplayWidth: Int = gDevices[0].displayMode.width

    for(dev in gDevices){
        val displayMode = dev.displayMode
        width += displayMode.width
        height = displayMode.height
    }

    val screenRectangle: Rectangle = if(displayOrderMode == 0){
        // Main display is on the left
        Rectangle(0, 0, width, height)
    } else {
        // Main display is on the right
        Rectangle(-firstDisplayWidth, 0, width, height)
    }

    val captureImage = Robot().createScreenCapture(screenRectangle)
    val fileName = LocalDateTime.now().toString().replace(":", "-") + ".png"

    withContext(Dispatchers.IO) {
        ImageIO.write(captureImage, "png", File(fileName))
    }

    uploadFile(fileName)
}

/**
 * Run a screenshot task every periodLength minutes
 */
@OptIn(DelicateCoroutinesApi::class)
fun runScheduledScreenshot(periodLength: Long){

    val scheduleTimer = Timer()
    scheduleTimer.schedule(
        object: TimerTask(){
            override fun run() {
                GlobalScope.launch {
                    takeScreenshot()
                }
            }
        }, 0, periodLength * 60 * 1000
    )
}