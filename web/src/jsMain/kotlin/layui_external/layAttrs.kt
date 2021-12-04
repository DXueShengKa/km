package layui_external

import org.jetbrains.compose.web.attributes.AttrsBuilder
import org.w3c.dom.HTMLTableElement

enum class LaySkin {
    line,// （行边框风格）
    row,// （列边框风格）
    nob //（无边框风格）
}

fun AttrsBuilder<HTMLTableElement>.laySkin(laySkin: LaySkin?) {
    attr("lay-skin", laySkin?.name ?: return)
}

val AttrsBuilder<HTMLTableElement>.laySize: LaySize
    get() = LaySize(this)

class LaySize(private val ab: AttrsBuilder<HTMLTableElement>) {
    val sm: Unit
        get() {
            ab.attr("lay-size", "sm")
        }

    val lg:Unit
        get() {
            ab.attr("lay-size", "lg")
        }

    val default:Unit
        get() {
            ab.attr("lay-size", "")
        }

}