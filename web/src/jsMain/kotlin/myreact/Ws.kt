package myreact

import com.km.common.api_impl.UserWs
import kotlinx.coroutines.launch
import mui.material.*
import react.*

external interface WsState:State{
    var list:MutableList<String>?
    var sendText:String
}

class WsUI: RenderComponent<UserWs, WsState>() {

    override fun componentDidMount() {
        component.flow.bindToState { txt ->
            println(txt)
            list = list?.also { it.add(txt) } ?: mutableListOf(txt)
        }
        super.componentDidMount()
    }

    override fun RBuilder.render() {
        mui.material.List{
            ListItem{
                state.list?.forEach {
                    ListItemText{
                        attrs.primary = ReactNode(it)
                    }
                }
            }

            ListItem{
                Input{
                    attrs {
                        onChange = {
                            println(it.target.asDynamic().value)
                            state.sendText = it.target.asDynamic().value as String
                        }
                    }
                }
                ListItemButton{
                    attrs.onClick = {
                        component.send(state.sendText)
                        println(state.sendText)
                    }
                    +"发送至后台"
                }
                ListItemButton{
                    attrs.onClick = {
                        scope.launch {
                            println(component.sendAll(state.sendText))
                        }
                    }
                    +"发送至全局"
                }
            }
        }
    }

}