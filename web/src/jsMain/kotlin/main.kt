
import androidx.compose.runtime.*
import com.km.common.alert.Alerts
import com.km.common.alert.IAlert
import com.km.common.api_impl.UserWs
import com.km.common.mainInit
import com.km.common.navigation.navTest.NavComponent
import com.km.common.navigation.navTest.NextComponent
import com.km.common.viewmodel.rememberComponentContext
import com.km.common.viewmodel.subscribeAsState
import kotlinext.js.jso
import kotlinx.browser.document
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import layui_external.LayUiStyle
import layui_external.LayuiModel
import layui_external.layer
import layui_external.layui
import layui_external.open
import myreact.App
import myreact.WsUI

import org.jetbrains.compose.web.attributes.disabled
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.renderComposable
import org.w3c.dom.Navigator
import react.*
import react.State
import react.dom.*
import react.dom.html.ButtonType
import react.dom.onClick
import rtc.PeerTest
import rtc.RTCSdpType
import rtc.rtcTest
import ui.NavLoginUI
import kotlin.js.Date
import kotlin.random.Random


fun main() {
    appInit()

//    renderComposable("root") {
//        NavCompose()
//        val root = rememberComponentContext(::NavComponent)
//    }

    document.title = "kt多平台web版"

    render(document.getElementById("root")){
        div {
//            child(App::class){}
            child(rtcTest)
//
//            child(WsUI::class){
//                attrs.component = UserWs("2")
//            }
        }
    }
}

fun appInit() {
    mainInit()

    Alerts.alert = object : IAlert {
        override fun msg(text: String) {
            layer.msg(text)
        }
    }

    layui.use(
        apps = arrayOf(LayuiModel.layer, LayuiModel.form, LayuiModel.element, LayuiModel.laydate)
    )

}



@Composable
fun LayUiTest() {

    Button({
        classes(LayUiStyle.btn(),LayUiStyle.btn.radius)
        onClick {
            layer.open{
                title = "title"
                type.msg("msg")
            }
        }
    }) {
        Text("11111")
    }

    Button({
        classes(LayUiStyle.btn(), LayUiStyle.btn.primary)

        onClick {
            layer.msg(Random.nextDouble().toString())
        }
    }) {
        Text("222222")
    }

    Button({
        id("333")
        classes(LayUiStyle.btn(), LayUiStyle.btn.primary)
        onClick {
            layer.open{
                type.tips("333","333")
            }
        }
    }) {

        Text("222222")
    }

    I({
        classes(LayUiStyle.icon(),LayUiStyle.icon.heartFill)
    })

    NavCompose()
}

enum class KmRoust(val conunt:@Composable ()->Unit){
    User({
//        ui.UserUi()
    }),
    UserList({
        ui.UserListUi()
    }),
    Video({
        ui.VideoUI()
    }),
    Login({
        ui.LoginUI()
    })
}

@Composable
fun NavCompose(){

    var uiThis by remember { mutableStateOf(KmRoust.User) }

    Ul({
        classes(LayUiStyle.nav())
    }){
        KmRoust.values().forEach { roust ->
            Li({
                onClick {
                    uiThis = roust
                }
                classes(LayUiStyle.nav.item)
                if (roust == uiThis){
                    classes(LayUiStyle.layuiThis)
                }
            }) {
                A {
                    Text("- $roust -")
                }
            }
        }

        NavLoginUI()
    }

    uiThis.conunt()

}





