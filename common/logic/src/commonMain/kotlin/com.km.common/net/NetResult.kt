package com.km.common.net

import kotlinx.serialization.Serializable

@Serializable
data class NetResult<T>(
    val data: T?,
    val code: UInt,
    val message: String,
) {
    companion object {
        fun <T> succeed(data: T): NetResult<T> {
            return NetResult(
                data,
                ResultState.SUCCEED.code,
                ResultState.SUCCEED.message
            )
        }

        fun <T> fail(throws: Throwable): NetResult<T> {
            return NetResult(
                null,
                ResultState.THROWABLE.code,
                throws.message ?: ResultState.THROWABLE.message
            )
        }

        fun <T> fail(code: UInt, message: String): NetResult<T> {
            return NetResult(
                null,
                code,
                message
            )
        }

        fun <T> fail(state: ResultState,data:T?=null): NetResult<T> {
            return NetResult(
                data,
                state.code,
                state.message
            )
        }
    }

    val isSucceed = code == ResultState.SUCCEED.code

    val succeedData:T
        get() = data!!
}