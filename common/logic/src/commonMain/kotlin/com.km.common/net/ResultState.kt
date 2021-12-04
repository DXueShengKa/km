package com.km.common.net

enum class ResultState(
    val code: UInt,
    val message: String,
) {
    OTHER(0u, ""),
    SUCCEED(1u, "成功"),
    THROWABLE(2u, "异常"),
    NOT_LOGIN(3u,"未登录");
}