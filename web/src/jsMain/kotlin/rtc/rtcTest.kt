package rtc

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.km.common.api_impl.Wss
import kotlinext.js.jso
import kotlinx.browser.document
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.await
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.dom.Video
import org.jetbrains.compose.web.renderComposable
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLVideoElement
import react.MutableRefObject
import react.Props
import react.createRef
import react.dom.div
import react.fc
import react.useEffect
import react.useLayoutEffectOnce
import react.useRef
import kotlin.js.Date



val rtcTest = fc<Props> {

    val containerRef = useRef(createRef<HTMLElement>().current)

    useCompose(
        containerRef = containerRef,
        stateInitialValue = 0,
        stateValueProvider = { 0 }
    ) {
        RtcCompose()
    }

    div {
        ref = containerRef
    }
}

@Composable
fun RtcCompose(){
    LaunchedEffect(true){
        PeerTest(this)
    }
    Video({
        id("rtcVideo")
        attr("controls","")
        attr("autoplay","")
    })
}

class PeerTest(
    scope: CoroutineScope = GlobalScope
): KoinComponent {
    private val peer:RTCPeerConnection = RTCPeerConnection()
    private val wss:Wss = Wss("/rtc",get(),scope)

    init {

        scope.launch {
            println("PeerTest init")
            createMedia()
            onMessage()
        }
    }

    private suspend fun createMedia(){
        val localStream = navigator.mediaDevices.getUserMedia(jso {
            video = true
        }).await()

        val v = document.getElementById("rtcVideo") as HTMLVideoElement
        v.srcObject = localStream

        peer.addStream(localStream)

        peer.onicecandidate = { event ->
            event.candidate?.also(::addIceCandidateB)
        }
    }

    private fun addIceCandidateB(candidate:RTCIceCandidate){
        wss.send(JSON.stringify(jso<NetData> {
            type = "addIceCandidateB"
            data = candidate
        }))
    }

    private suspend fun setRemoteDescriptionA(desc: RTCSessionDescriptionInit):RTCSessionDescriptionInit{
        peer.setRemoteDescription(desc).await()

        val answer = peer.createAnswer().await()

        wss.send(JSON.stringify(jso<NetData> {
            type = "onCreateAnswer"
            data = answer
        }))

        peer.setLocalDescription(answer).await()

        return answer
    }

    private fun hangup() {
        peer.close()
    }

    private suspend fun onMessage(){
        wss.flow.collect {
            val nd = JSON.parse<NetData>(it)

            when(nd.type){
                "addIceCandidateA" -> peer.addIceCandidate(nd.data)
                "hangup" -> hangup()
                "setRemoteDescriptionA" -> setRemoteDescriptionA(nd.data)
                else -> {

                }
            }
        }
    }
}

private external interface NetData{
    var type:String
    var data:dynamic
}


/**
 * 在react中使用compose
 * @param containerRef - [MutableRefObject] - reference to the HTMLElement that is used as a root for Composition
 * @param stateInitialValue - initial state value for the Composition
 * @param stateValueProvider - a lambda that's used to change the state's value
 * @param composable - the content controlled by Compose and mounted in a root provided by [containerRef]
 */
 fun <T> useCompose(
    containerRef: MutableRefObject<HTMLElement>,
    stateInitialValue: T,
    stateValueProvider: () -> T,
    composable: @Composable (state: State<T>) -> Unit
) {
    val mutableState = useRef(mutableStateOf(stateInitialValue))

    useEffect {
        mutableState.current?.value = stateValueProvider()
    }

    useLayoutEffectOnce{
        val composition = renderComposable(containerRef.current!!) {
            composable(mutableState.current!!)
        }
        cleanup {
            composition.dispose()
        }
    }

}
