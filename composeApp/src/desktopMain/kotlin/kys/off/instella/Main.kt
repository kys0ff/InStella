package kys.off.instella

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kys.off.instella.ui.main.MainScreen

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication
    ) {
        MainScreen()
    }
}