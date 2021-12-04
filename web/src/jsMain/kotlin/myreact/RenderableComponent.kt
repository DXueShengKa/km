package myreact

import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.ValueObserver
import csstype.HtmlAttributes
import kotlinext.js.Object
import kotlinext.js.jsObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.promise
import kotlinx.css.data
import react.*
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.reflect.KClass


var uniqueId: Long = 0L

internal fun Any.uniqueId(): Long {
    var id: dynamic = asDynamic().__unique_id
    if (id == undefined) {
        id = ++uniqueId
        Object.defineProperty<Any, Long>(this, "__unique_id", jsObject { value = id })
    }
    return id
}

external interface RProps<T> : Props{
    var component: T
}

abstract class RenderComponent<T : Any, S : State> : RComponent<RProps<T>, S>() {

    protected val component: T get() = props.component
    protected val scope = CoroutineScope(EmptyCoroutineContext)

    private val subscriptions = ArrayList<Subscription<*>>()

    override fun componentDidMount() {
        subscriptions.forEach { subscribe(it) }
    }

    private fun <T : Any> subscribe(subscription: Subscription<T>) {
        subscription.value.subscribe(subscription.observer)
    }

    override fun componentWillUnmount() {
        subscriptions.forEach { unsubscribe(it) }
        scope.cancel()
    }

    private fun <T : Any> unsubscribe(subscription: Subscription<T>) {
        subscription.value.unsubscribe(subscription.observer)
    }

    protected fun <T : Any> Value<T>.bindToState(buildState: S.(T) -> Unit) {
        subscriptions += Subscription(this) { data -> setState { buildState(data) } }
    }

    protected fun <T> Flow<T>.bindToState(buildState: S.(T) -> Unit) {
        scope.launch {
            collect { data -> setState{ buildState(data) } }
        }
    }

    fun <M : Any, T : RenderComponent<M, *>> RBuilder.renderChild(clazz: KClass<out T>, model: M?) {
        model?:return
        child(clazz) {
            key = model.uniqueId().toString().also { println(it) }
            attrs.component = model
        }
    }

    fun < C : Any,  T : Any> RouterState<C,T>.childInstance():T? {
        return takeIf { asDynamic() != undefined }?.activeChild?.instance
    }

    protected class Subscription<T : Any>(
        val value: Value<T>,
        val observer: ValueObserver<T>
    )
}