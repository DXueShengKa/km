package ui

import androidx.compose.runtime.Composable
import layui_external.LayUiStyle
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.ContentBuilder
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Label
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.HTMLDivElement


/*@Composable
fun UserUi() {
    val vm = viewModels<UserViewModel>()

    Form(attrs = {
        classes(LayUiStyle.form())
    }) {

        LayFormItem("用户名") {

            Input(InputType.Text) {
                onInput {
                    vm.apply {
                        userInfo = userInfo.copy(name = it.value)
                    }
                }
                classes(LayUiStyle.input())
                value(vm.userInfo.name)
                placeholder("请输入用户名")
            }

        }

        LayFormItem("密码") {

            Input(InputType.Password) {
                onInput {
                    vm.apply {
                        userInfo = userInfo.copy(password = it.value)
                    }
                }
                classes(LayUiStyle.input())
                value(vm.userInfo.password)
                placeholder("请输入密码")
            }
        }

        LayFormItem("邮箱") {

            Input(InputType.Email) {
                onInput {
                    vm.apply {
                        userInfo = userInfo.copy(email = it.value)
                    }
                }
                classes(LayUiStyle.input())
                value(vm.userInfo.email)
                placeholder("请输入邮箱")
            }
        }

        LayFormItem("手机号") {

            Input(InputType.Text) {
                onInput {
                    vm.apply {
                        userInfo = userInfo.copy(phone = it.value)
                    }
                }
                classes(LayUiStyle.input())
                value(vm.userInfo.phone)
                placeholder("请输入手机号")
            }
        }

        LayFormItem("生日") {
            Input(InputType.Date) {
                id("birthday")
                onInput {
                    vm.birthday = it.value
                }
                classes(LayUiStyle.input())
                value(vm.birthday)
                placeholder("请选择日期")
            }
        }

        LayFormItem {
            Div({
                classes(LayUiStyle.btn())
                onClick {
                    vm.getData()
                }

            }) {
                Text("提交")
            }
        }
    }
}*/


@Composable
private fun LayFormLabel(label: String) {
    Label(attrs = {
        classes(LayUiStyle.form.label)
    }) {
        Text(label)
    }
}


@Composable
fun LayFormItem(label: String? = null, content: ContentBuilder<HTMLDivElement>) {
    Div({
        classes(LayUiStyle.form.item)
        style {
            width(400.px)
        }
    }) {
        if (label != null)
            LayFormLabel(label)
        Div({ classes(LayUiStyle.input.block) }, content)
    }
}


