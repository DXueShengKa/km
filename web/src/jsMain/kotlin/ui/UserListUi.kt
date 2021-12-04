package ui

import androidx.compose.runtime.*
import com.km.common.entity.UserInfo
import layui_external.LaySkin
import layui_external.LayUiStyle
import layui_external.laySize
import layui_external.laySkin
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.*


@Composable
fun UserListUi() {


//    val vm = viewModels<UserListViewModel>()


    Button({
        classes(LayUiStyle.btn())
        onClick {
//            vm.getData()
        }
    }) {
        Text("列表")
    }

    var laySkin by remember { mutableStateOf(LaySkin.line) }
    LaySkin.values().forEach { l ->
        Button({
            classes(LayUiStyle.btn(), LayUiStyle.btn.radius)
            onClick {
                laySkin = l
            }
        }) {
            Text(l.name)
        }
    }

    var ls by remember { mutableStateOf(1) }
    repeat(3) { l ->
        Button({
            classes(LayUiStyle.btn(), LayUiStyle.btn.primary)
            onClick {
                ls = l
            }
        }) {
            Text(l.toString())
        }
    }

    Table({
        classes(LayUiStyle.table())
        laySkin(laySkin)
        when(ls){
            0->laySize.default
            1->laySize.lg
            2->laySize.sm
        }
    }) {
        Colgroup {
            Col {
                style { width(100.px) }
            }
            Col {
                style { width(150.px) }
            }
            Col { }
        }
        Thead {
            Tr {
                Th { Text("姓名") }
                Th { Text("邮箱") }
                Th { Text("生日") }
            }
        }
        Tbody {
//            vm.users.forEach {
//                TB(it)
//            }
        }
    }
}

@Composable
private fun TB(userInfo: UserInfo) {
    Tr {
        Td {
            Text(userInfo.name)
        }
        Td {
            Text(userInfo.email)
        }
        Td {
            Text(userInfo.birthday.toString())
        }
    }
}
