package com.km.repository

import com.km.entity.DBUserInfo
import org.springframework.data.jpa.repository.JpaRepository

interface UserInfoRepository : JpaRepository<DBUserInfo, Int> {
}