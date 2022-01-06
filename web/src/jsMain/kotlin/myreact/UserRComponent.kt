package myreact

import com.km.common.entity.UserInfo
import com.km.common.navigation.user.UserInfoComponent
import com.km.common.navigation.user.UserListComponent
import com.km.common.navigation.user.UserRootComponent
import csstype.NamedColor
import csstype.px
import kotlinext.js.jso
import mui.material.*
import react.BoxedState
import react.RBuilder
import react.ReactNode

class UserRootRComponent : RenderComponent<UserRootComponent, BoxedState<UserRootComponent.RC>>() {

    override fun componentDidMount() {
        component.router.bindToState {
            state = it.activeChild.instance
        }
        super.componentDidMount()
    }

    override fun RBuilder.render() {

        when (val rc = state.state) {
            is UserListComponent -> child(UserListRComponent::class) {
                attrs.component = rc
            }
            is UserInfoComponent -> child(UserInfoRComponent::class) {
                attrs.component = rc
            }
        }
    }

}

class UserListRComponent : RenderComponent<UserListComponent, BoxedState<List<UserInfo>?>>() {

    override fun componentDidMount() {
        component.users.bindToState {
            state = it
        }
        super.componentDidMount()
    }

    override fun RBuilder.render() {
        List {
            state.state?.forEach { user ->
                ListItemButton {
                    attrs.onClick = {
                        props.component.toUserInfo(user.id)
                    }

                    ListItemText{
                        attrs {
                            primary = ReactNode("${user.name} - ${user.email}")
                        }
                    }

                }
            }
        }
    }

}


class UserInfoRComponent : RenderComponent<UserInfoComponent, BoxedState<UserInfo?>>() {


    override fun componentDidMount() {

        component.user.bindToState {
            state = it
        }

        super.componentDidMount()
    }

    override fun RBuilder.render() {

        Card {
            attrs {
                sx = jso {
                    width = 400.px
                }
            }

            CardContent {
                attrs.sx = jso {
                    color = NamedColor.red
                }
                +"${state.state}"
            }

            CardActions {
                Button {
                    attrs.onClick = {
                        component.onBack()
                    }
                    attrs.size = Size.small
                    +"返回"
                }
            }
        }


    }

}

