package com.km.common.navigation.navTest

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.km.common.navigation.master.disposableScope
import kotlinx.coroutines.*
import kotlinx.coroutines.GlobalScope.coroutineContext
import kotlinx.coroutines.flow.*

class NextComponent(
    val id:Int,
    val name:String,
    componentContext: ComponentContext,
    private val coroutineScope: CoroutineScope = componentContext.disposableScope()
) : ComponentContext by componentContext {

    val data = "id->$id name->$name"

  /*  val value:StateFlow<String> = flow {
        var i = 0
        while (currentCoroutineContext().isActive){
            emit(i++.toString())
            delay(1000)
        }
    }.stateIn(coroutineScope, SharingStarted.Lazily,"")*/

    val v = MutableValue("").also {
        coroutineScope.launch {
            var i = 0
            while (isActive){
                it.value = (i++.toString())
                delay(1000)
            }
        }
    }
}
