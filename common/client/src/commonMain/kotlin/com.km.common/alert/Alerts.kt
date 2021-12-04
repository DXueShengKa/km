package com.km.common.alert

object Alerts {
    lateinit var alert:IAlert


    fun msg(text:String){
        alert.msg(text)
    }
}