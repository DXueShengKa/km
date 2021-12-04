package com.km

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.arkivanov.decompose.defaultComponentContext
import com.km.common.Constant
import com.km.common.api.IUserApi
import com.km.common.navigation.user.UserRootComponent
import com.km.common_ui.UserRootUI
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Toast.makeText(this, Constant.currentEnvironment,Toast.LENGTH_SHORT).show()

//        val root = CounterRootComponent(defaultComponentContext())
//        val root = RootComponent(defaultComponentContext())
//        val root = UserRootComponent(defaultComponentContext())

        setContent {
            MaterialTheme {
                Column {
                    Text(com.km.processor.KspTest.string)
//                    Box(Modifier.weight(1f)){
//                        UserRootUI(root)
//                    }
//                    Box(Modifier.weight(1f)){
//                        WsUI()
//                    }
                }
            }
        }
    }
}