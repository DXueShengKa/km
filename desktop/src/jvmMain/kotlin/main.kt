import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.lifecycle.LifecycleController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.km.common.mainInit
import com.km.common.navigation.user.UserRootComponent
import com.km.common_ui.UserRootUI
import com.km.common_ui.WsUI


@OptIn(ExperimentalDecomposeApi::class)
fun main() {
    mainInit()
    val lifecycle = LifecycleRegistry()
//    val root = CounterRootComponent(DefaultComponentContext(lifecycle))
//    val root = RootComponent(DefaultComponentContext(lifecycle))
    val root = UserRootComponent(DefaultComponentContext(lifecycle))

    application {
        val windowState = rememberWindowState()
        LifecycleController(lifecycle, windowState)
        Window(
            title = "kt多平台pc版",
            state = windowState,
            onCloseRequest = ::exitApplication
        ) {

            MaterialTheme(
                if (isSystemInDarkTheme()) darkColors() else lightColors()
            ) {
//                CounterRootUi(root)
                Row {
//                    Box(Modifier.weight(1f)){
                    UserRootUI(root)
//                    }
//                    Box(Modifier.weight(1f)){
//                        WsUI()
//                    }
                }
            }
        }
    }
}

//fun main() {
//    println(com.km.processor.KspTest.string)
//}