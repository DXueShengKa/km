package com.km.common.entity

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    var id: Int = 0,
    var name: String = "",
    var phone: String = "",
    var email: String = "",
    var password: String = "",
    var birthday: LocalDate? = null,
)