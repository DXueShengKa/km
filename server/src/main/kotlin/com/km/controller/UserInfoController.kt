package com.km.controller

import com.km.common.api.IUserApi
import com.km.common.entity.UserInfo
import com.km.common.net.NetResult
import com.km.entity.DBUserInfo
import com.km.repository.UserInfoRepository
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toKotlinLocalDate
import org.springframework.beans.BeanUtils
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping(IUserApi.user)
class UserInfoController(
    private val userInfoRepository: UserInfoRepository,
) {

    companion object{

        @JvmStatic
        fun dbToUiEntity(dbUserInfo: DBUserInfo):UserInfo{
            val ui = UserInfo()
            BeanUtils.copyProperties(dbUserInfo, ui,"password")
            ui.birthday = dbUserInfo.birthday?.toKotlinLocalDate()
            return ui
        }

    }

    @GetMapping(IUserApi.list)
    fun userList(): NetResult<List<UserInfo>> {

        val users = userInfoRepository.findAll().map(::dbToUiEntity)

        return NetResult.succeed(users)
    }




    @PostMapping
    fun saveUser(userInfo: UserInfo): NetResult<UserInfo> {

        try {
            var db = DBUserInfo()
            BeanUtils.copyProperties(userInfo, db)
            db.birthday = userInfo.birthday?.toJavaLocalDate()

            db = userInfoRepository.save(db)

            return NetResult.succeed(dbToUiEntity(db))
        } catch (e: Exception) {
            return NetResult.fail(e)
        }

    }

    @GetMapping("/{userId}")
    fun getUser(@PathVariable userId: Int): NetResult<UserInfo> {
        try {
            val ui = dbToUiEntity(userInfoRepository.getById(userId))

            return NetResult.succeed(ui)
        } catch (e: Exception) {
            return NetResult.fail(e)
        }
    }

    @DeleteMapping("/{userId}")
    fun deleteUser(@PathVariable userId: Int): NetResult<String> {
        try {
            return NetResult.succeed("删除成功")
        } catch (e: Exception) {
            return NetResult.fail(e)
        }
    }

}