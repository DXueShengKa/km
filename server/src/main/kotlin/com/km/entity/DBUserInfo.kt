package com.km.entity


import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity(
    name = "user_info"
)
class DBUserInfo(
    @Id
    @GeneratedValue
    var id: Int? = null,

    @Column(nullable = false, length = 24)
    var name: String? = null,

    @Column(nullable = false, length = 32)
    var password: String? = null,

    var birthday: LocalDate? = null,

    @Column(length = 11)
    var phone: String? = null,

    @Column(length = 24)
    var email: String? = null,


    @Column(
        nullable = false,
        updatable = false,
        insertable = false,
        columnDefinition = "datetime default current_timestamp"
    )
    var createTime: LocalDateTime? = null,


    @Column(
        nullable = false,
        updatable = false,
        insertable = false,
        columnDefinition = "datetime default current_timestamp on update current_timestamp"
    )
    var uploadTime: LocalDateTime? = null,
) {

    override fun toString(): String {
        return "DBUserInfo(id=$id, name=$name, birthday=$birthday, phone=$phone, email=$email, password=$password, createTime=$createTime, uploadTime=$uploadTime)"
    }

}