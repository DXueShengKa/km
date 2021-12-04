package ui

import androidx.compose.runtime.Composable
import com.km.common.net.KmBaseUrl
import org.jetbrains.compose.web.dom.Video


@Composable
fun VideoUI(){
    Video({
        id("v")
        ref {
            it.src = "${KmBaseUrl.url}/file/player"
            it.controls = true
            it.preload = "auto"
            it.height = 720
            it.width = 1240
            onDispose {

            }
        }
    })

}