package com.km.common_ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.km.common.api_impl.UserWs
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WsUI() {

    val list = remember { mutableStateListOf<String>() }
    val ws = remember { UserWs("1") }

    LaunchedEffect(Unit) {
        ws.flow.collect {
            list.add(it)
        }
    }

    Column {
        LazyColumn(Modifier.weight(1f)) {
            items(list) { text ->
                ListItem {
                    Text(text)
                }
            }
        }

        val cc = rememberCoroutineScope()
        var text1 by remember { mutableStateOf("") }
        OutlinedTextField(
            text1,
            { text1 = it },
            trailingIcon = {
                Icon(Icons.Default.Call, null, Modifier.clickable {
                    cc.launch {
                        println(ws.sendAll(text1))
                    }
                })
            }
        )

        var text by remember { mutableStateOf("") }
        OutlinedTextField(
            text,
            { text = it },
            trailingIcon = {
                Icon(Icons.Default.Send, null, Modifier.clickable {
                   ws.send(text)
                })
            }
        )
    }

}