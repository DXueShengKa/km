package com.km.common_ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.slide
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.km.common.navigation.user.UserInfoComponent
import com.km.common.navigation.user.UserListComponent
import com.km.common.navigation.user.UserRootComponent


@Composable
fun UserRootUI(root: UserRootComponent){
    val routerState = root.router.subscribeAsState().value
    Children(routerState, animation = slide()){
        when(val i = it.instance){
            is UserListComponent -> UserListUI(i)
            is UserInfoComponent -> UserInfoUI(i)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UserListUI(userList: UserListComponent){
    val users = userList.users.subscribeAsState().value
    LazyColumn {
        items(users){ user ->
            ListItem(
                Modifier.clickable {
                    userList.toUserInfo(user.id)
                },
                icon = {
                    Text(user.id.toString())
                },
                secondaryText = {
                    Text(user.email)
                },
                text = {
                    Text(user.name)
                }
            )
        }
    }
}

@Composable
fun UserInfoUI(userInfoComponent: UserInfoComponent){
    val user = userInfoComponent.user.collectAsState().value
    Card(Modifier.padding(10.dp)){
        Column {
            Text(
                user?.toString()?:"k空",
                color = Color.Red,
                fontSize = 30.sp
            )
            Button({
                userInfoComponent.onBack()
            }){
                Text("返回")
            }
        }
    }
}