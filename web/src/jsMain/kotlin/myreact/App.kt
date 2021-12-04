package myreact

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.resume

import com.km.common.navigation.user.UserRootComponent
import react.PropsWithChildren
import react.RBuilder
import react.RComponent
import react.State


class App : RComponent<PropsWithChildren, State>() {

    private val lifecycle = LifecycleRegistry()
    private val ctx = DefaultComponentContext(lifecycle = lifecycle)
    private val root = UserRootComponent(ctx)

    override fun componentDidMount() {
        lifecycle.resume()
    }

    override fun componentWillUnmount() {
        lifecycle.destroy()
    }

    override fun RBuilder.render() {
        child(UserRootRComponent::class){
            attrs.component = root
        }
    }
}
