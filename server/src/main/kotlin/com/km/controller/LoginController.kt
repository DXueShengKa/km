package com.km.controller

import com.km.common.api.ILoginApi
import com.km.common.entity.UserInfo
import com.km.common.net.NetResult
import com.km.common.net.ResultState
import com.km.entity.DBUserInfo
import com.km.repository.UserInfoRepository
import org.springframework.data.domain.Example
import org.springframework.data.domain.ExampleMatcher
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession

@RestController
@RequestMapping(ILoginApi.login)
class LoginController(
    private val userInfoRepository: UserInfoRepository,
) {
    companion object {
        const val LOGIN_USER = "LoginUser"
    }

   /* @GetMapping
    fun get(session: HttpSession): NetResult<UserInfo> {
        val userInfo = session.getAttribute(LOGIN_USER) as? UserInfo

        return if (userInfo == null)
            NetResult.fail(ResultState.NOT_LOGIN)
        else
            NetResult.succeed(userInfo)
    }*/

    @GetMapping
    fun get(request: HttpServletRequest,response: HttpServletResponse): NetResult<UserInfo> {
        val session: HttpSession = request.session
        request.headerNames.asIterator().forEach {
            println("$it ${request.getHeader(it)}")
        }

        val userInfo = session.getAttribute(LOGIN_USER) as? UserInfo

        println("-------------------")

        response.headerNames.forEach {
            println("$it ${response.getHeader(it)}")
        }
        return if (userInfo == null)
            NetResult.fail(ResultState.NOT_LOGIN)
        else
            NetResult.succeed(userInfo)
    }



    @PostMapping
    fun login(username: String, password: String, session: HttpSession): NetResult<UserInfo> {
        val dbUserInfo = DBUserInfo(
            name = username
        )
        val optional =
            userInfoRepository.findOne(Example.of(dbUserInfo, ExampleMatcher.matching().withIgnoreNullValues()))

        return if (optional.isEmpty)
            NetResult.fail(0u, "该用户不存在")
        else {
            val user = optional.get()

            if (user.password != password)
                return NetResult.Companion.fail(0u,"账号密码错误")

            return UserInfoController.dbToUiEntity(user).let {
                session.setAttribute(LOGIN_USER, it)
                NetResult.succeed(it)
            }

        }

    }

    @GetMapping(ILoginApi.login)
    fun logout(session: HttpSession): NetResult<String> {
        val any:Any? = session.getAttribute(LOGIN_USER)
        return if (any == null){
            NetResult.Companion.fail(ResultState.NOT_LOGIN)
        }else{
            NetResult.succeed("已注销")
        }
    }
}