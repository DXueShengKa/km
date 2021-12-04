package layui_external


object LayuiModel {
    const val layer = "layer"
    const val form = "form"
    const val element = "element"
    const val table = "table"
    const val laydate = "laydate"
}


external interface Layui {
    fun use(apps: Array<String>, callback: () -> Unit = definedExternally, exports:dynamic = definedExternally, from:dynamic = definedExternally)

    val laydate:Laydate
}


val layui: Layui = js("window.layui")