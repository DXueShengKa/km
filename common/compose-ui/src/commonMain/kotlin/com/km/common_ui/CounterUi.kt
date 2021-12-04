package com.km.common_ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.km.common.navigation.counter.Counter

@Composable
fun CounterUi(counter: Counter) {
    val model by counter.model.subscribeAsState()
    Box(
        modifier = Modifier
            .border(BorderStroke(width = 1.dp, color = Color.Black))
            .padding(16.dp)
    ) {
        Text(text = model)
    }
}

/*
@Preview(showBackground = true)
@Composable
fun CounterUiPreview() {
    CounterUi(counter = CounterPreview())
}

class CounterPreview : Counter {
    override val model: Value<Counter.Model> = MutableValue(Counter.Model(text = "53"))
}
*/
