package layui_external

import org.jetbrains.compose.web.css.StyleSheet

object LayUiStyle : StyleSheet() {

    object btn {
        operator fun invoke() = "layui-btn"
        val radius = invoke() + "-radius"
        val primary = invoke() + "-primary"
    }

    object nav {
        operator fun invoke() = "layui-nav"
        val item = invoke() + "-item"
        val child = invoke() + "-child"
    }

    object icon {
        operator fun invoke() = "layui-icon"

        val heartFill = invoke() + "-heart-fill"
    }

    object input {
        operator fun invoke() = "layui-input"
        val block = invoke() + "-block"
    }

    object form {
        operator fun invoke() = "layui-form"
        val item = invoke() + "-item"
        val label = invoke() + "-label"
    }

    object table{
        operator fun invoke() = "layui-table"

    }

    const val layuiThis = "layui-this"

    const val container = "layui-container"


}