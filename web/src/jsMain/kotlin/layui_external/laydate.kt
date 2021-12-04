package layui_external

import org.w3c.dom.Element
import kotlin.js.Date


external interface Laydate {
    fun render(render: Render)
}

fun Laydate.render(builder: Render.() -> Unit) {
    val render: Render = js("{}")
    render.apply(builder)
    render(render)
}

external interface Render {
    var elem: Element
    var value: Date

    /**
    类型：string，默认值：min: '1900-1-1'、max: '2099-12-31'

    设定有限范围内的日期或时间值，不在范围内的将不可选中。这两个参数的赋值非常灵活，主要有以下几种情况：

    1.	如果值为字符类型，则：年月日必须用 -（中划线）分割、时分秒必须用 :（半角冒号）号分割。这里并非遵循 format 设定的格式
    2.	如果值为整数类型，且数字＜86400000，则数字代表天数，如：min: -7，即代表最小日期在7天前，正数代表若干天后
    3.	如果值为整数类型，且数字 ≥ 86400000，则数字代表时间戳，如：max: 4073558400000，即代表最大日期在：公元3000年1月1日
     */
    var min: dynamic
    var max: dynamic

    /**
     * 默认显示
     */
    var show: Boolean

    /**
     * 我们内置了一些我国通用的公历重要节日，通过设置 true 来开启。国际版不会显示。
     */
    var calendar: Boolean
}