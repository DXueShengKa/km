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
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.km.common.navigation.counter.CounterInner


@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun CounterInnerUi(counterInner: CounterInner) {
    Box(modifier = Modifier.border(BorderStroke(width = 1.dp, color = Color.Green))) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            CounterUi(counterInner.counter)

            Spacer(modifier = Modifier.height(16.dp))

            Row {

                Column(
                    modifier = Modifier.clipToBounds(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val activeChild = counterInner.leftRouterState.subscribeAsState().value.activeChild.instance

                    ChildButtons(
                        isBackEnabled = activeChild.isBackEnabled,
                        onNext = counterInner::onNextLeftChild,
                        onPrev = counterInner::onPrevLeftChild
                    )

                    Children(routerState = counterInner.leftRouterState, animation = slide()) {
                        CounterUi(it.instance.counter)
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.clipToBounds(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val activeChild = counterInner.rightRouterState.subscribeAsState().value.activeChild.instance

                    ChildButtons(
                        isBackEnabled = activeChild.isBackEnabled,
                        onNext = counterInner::onNextRightChild,
                        onPrev = counterInner::onPrevRightChild
                    )

                    Children(routerState = counterInner.rightRouterState, animation = slide()) {
                        CounterUi(it.instance.counter)
                    }
                }
            }
        }
    }
}

@Composable
private fun ChildButtons(
    isBackEnabled: Boolean,
    onNext: () -> Unit,
    onPrev: () -> Unit
) {
    Column {
        Button(onClick = onNext) {
            Text(text = "下一个计数器")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onPrev, enabled = isBackEnabled) {
            Text(text = "上一个计数器")
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
