package layui_external

import kotlin.js.Json
import kotlin.js.json

external object layer {
    fun msg(message: String,layerAlert:LayerAlert = definedExternally)
    fun alert(message: String,layerAlert:LayerAlert = definedExternally)
    fun open(open: Json)

}

external interface LayerAlert{
    var icon:Int
    var title:String
}

fun layer.msg(message: String,alertBuilder: LayerAlert.() -> Unit) {
    val alert:LayerAlert = js("{}")
    alert.apply(alertBuilder)
    msg(message,alert)
}

fun layer.alert(message: String,alertBuilder: LayerAlert.() -> Unit) {
    val alert:LayerAlert = js("{}")
    alert.apply(alertBuilder)
    alert(message,alert)
}

fun layer.open(openBuilder: OpenOptions.() -> Unit) {

    val options = OpenOptions()
    options.apply(openBuilder)

    open(json(
        "title" to options.title,
        "content" to options.content,
        "type" to options.typeInt,
        "cancel" to options.cancel
    ).apply {

        options.cancel?.also {
            set("cancel",it)
        }

        options.btns.takeIf { it.isNotEmpty() }?.also { btns ->
            set("btn", btns.map { it.first }.toTypedArray())
            set("yes", btns[0].second)
            for (i in btns.indices) {
                if (i == 0) continue
                set("btn" + (i + 1), btns[i].second)
            }
        }

    })
}

class OpenOptions(
    var title: String? = null,
    var content: Any = "",
    var typeInt: Int = 0,
    var btns: List<Pair<String, () -> Unit>> = emptyList(),
    var cancel:(()->Boolean)? = null
){
    fun btns(vararg btns:Pair<String, () -> Unit>){
        this.btns = listOf(*btns)
    }

    val type = Type()

    inner class Type{

        fun msg(content:String){
            this@OpenOptions.content = content
            typeInt = 0
        }

        fun tips(content:String,domId:String){
            this@OpenOptions.content = arrayOf(content,"#$domId")
            typeInt = 4
        }
    }

}

/*value class OpenType(val typeInt: Int) {
    companion object {
        *//**
         * 信息框，默认
         *//*
        val msg = OpenType(0)

        *//**
         * 页面层
         *//*
        val page = OpenType(1)

        *//**
         * iframe层
         *//*
        val iframe = OpenType(2)

        *//**
         * 加载层
         *//*
        val load = OpenType(3)

        *//**
         * tips层,如果使用了这个类型则content为数组 arrayOf("333","#333")
         *//*
        val tips = OpenType(4)
    }
}*/
