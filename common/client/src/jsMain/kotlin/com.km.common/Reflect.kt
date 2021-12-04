package com.km.common

external object Reflect {
    fun construct(target: JsClass<dynamic>, argumentsList: Array<Any>): dynamic
}
