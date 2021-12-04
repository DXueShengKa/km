package ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import com.km.common.entity.UserInfo
import element_external.DD
import element_external.Dl
import layui_external.LayUiStyle
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.attributes.placeholder
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Form
import org.jetbrains.compose.web.dom.Input
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.Text
import kotlin.random.Random

@Composable
fun LoginUI(vm: LoginViewModel = remember { LoginViewModel() }) {

    Form(attrs = {
        classes(LayUiStyle.form())
    }) {
        LayFormItem("用户名") {

            Input(InputType.Text) {
                onInput {
                    vm.username = it.value
                }
                classes(LayUiStyle.input())
                value(vm.username)
                placeholder("请输入用户名")
            }

        }

        LayFormItem("密码") {

            Input(InputType.Password) {
                onInput {
                    vm.passoord = it.value
                }
                classes(LayUiStyle.input())
                value(vm.passoord)
                placeholder("请输入密码")
            }
        }

        LayFormItem {
            Div({
                classes(LayUiStyle.btn(), LayUiStyle.btn.primary)
                onClick {
                    vm.login()
                }

            }) {
                Text("登陆")
            }
        }
    }


    Text(LocalLoginUser2.current.toString())
}

@Composable
fun NavLoginUI() {

    Li({
        classes(LayUiStyle.nav.item)
    }) {
        A(href = "javascript:;") {
            val user = LocalLoginUser.current
            if (user == null) {
                Text("未登录")
            } else {
                Text(user.name)
            }
        }

        Dl({
            classes(LayUiStyle.nav.child)
        }) {
            DD {
                A(href = "javascript:;") {
                    Text("用户信息退出")
                }
            }
            DD {
                A(href = "javascript:;") {
                    Text("退出")
                }
            }
        }
    }
}

val LocalLoginUser = compositionLocalOf<UserInfo?> { null }

val LocalLoginUser2 = staticCompositionLocalOf { 0 }

class LoginViewModel  {

    var username by mutableStateOf("")
    var passoord by mutableStateOf("")

   /* init {
        viewModelScope.launch {
            val result = LoginApi.loginInfo()
            if (result.isSucceed) {
                LocalLoginUser.provides(result.data)
                println(result)
            } else {
                layer.alert(result.message)
            }
        }
    }
*/

    fun login() {

        LocalLoginUser2.provides(Random.nextInt())
       /* viewModelScope.launch {
            val result = LoginApi.login(username, passoord)

            if (result.isSucceed) {
                LocalLoginUser.provides(result.data)
            }

            layer.alert(result.message){
                title = "登陆"
            }
        }*/
    }


}
