package rtc

import org.w3c.dom.Navigator
import org.w3c.dom.mediacapture.MediaStream
import kotlin.js.Promise

fun RTCPeerConnection(): RTCPeerConnection =
    js("new (window.RTCPeerConnection || window.mozRTCPeerConnection || window.webkitRTCPeerConnection)")


external interface RTCPeerConnection {
    var onicecandidate: ((RTCPeerConnectionIceEvent) -> Unit)?
    fun setLocalDescription(description: RTCSessionDescriptionInit): Promise<Unit>
    fun setRemoteDescription(description: RTCSessionDescriptionInit): Promise<Unit>
    fun addIceCandidate(candidate: RTCIceCandidateInit?)
    fun createOffer(options: RTCOfferOptions?): Promise<RTCSessionDescriptionInit>
    fun onaddstream(event: StreamEvent)
    fun addStream(mediaStream: MediaStream)
    fun createAnswer(options: RTCAnswerOptions? = definedExternally): Promise<RTCSessionDescriptionInit>
    fun close()
}

external interface RTCAnswerOptions

external interface StreamEvent {
    var stream: MediaStream
}

external interface RTCSessionDescriptionInit {
    var sdp: String?
    var type: RTCSdpType?
}

/**
 * answer, offer, pranswer, rollback
 */
typealias RTCSdpType = String


external interface RTCPeerConnectionIceEvent {
    var candidate: RTCIceCandidate?
}

external interface RTCIceCandidate {
    var address: String?
    var candidate: String
    var component: dynamic
    var foundation: String?
    var port: Int?
    var priority: Int?
    var protocol: dynamic
    var relatedAddress: String?
    var relatedPort: Int?
    var sdpMLineIndex: Int?
    var sdpMid: String?
    var tcpType: dynamic
    var type: dynamic
    var usernameFragment: String
    fun toJSON(): RTCIceCandidateInit
}

external interface RTCIceCandidateInit {
    var candidate: String?
    var sdpMLineIndex: Int?
    var sdpMid: String?
    var usernameFragment: String?
}

external interface RTCOfferOptions {
    var iceRestart: Boolean?
    var offerToReceiveAudio: Boolean?
    var offerToReceiveVideo: Boolean?
}

external val navigator: Navigator