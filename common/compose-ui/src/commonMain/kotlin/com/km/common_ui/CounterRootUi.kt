package com.km.common_ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.slide
import com.km.common.navigation.counter.CounterRoot

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun CounterRootUi(counterRoot: CounterRoot) {
    Box(modifier = Modifier.border(BorderStroke(width = 1.dp, color = Color.Red))) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .clipToBounds()
                .padding(16.dp)
        ) {
            CounterUi(counterRoot.counter)

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = counterRoot::onNextChild) {
                Text(text = "Next Child")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Children(routerState = counterRoot.routerState, animation = slide()) {
                CounterInnerUi(it.instance.inner)
            }
        }
    }
}