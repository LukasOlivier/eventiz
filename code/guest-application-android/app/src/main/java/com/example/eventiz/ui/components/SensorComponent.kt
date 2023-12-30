package com.example.eventiz.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SensorComponent(modifier: Modifier, title: String, unit : String, value: Float, dangerousValue: Boolean) {
    val valueSize = 50.sp
    val unitSize = 15.sp
    val textColor = if (dangerousValue) {
       // Orange
        Color(0xFFFFA500)
    } else {
        Color.White
    }
    Card(
        modifier = modifier
    ) {
        Text(text = title, modifier = Modifier.padding(start = 10.dp, top = 10.dp), color = textColor)

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Row {
                Text(
                    text = value.toString(),
                    fontSize = valueSize,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 10.dp),
                    color = textColor
                )
                Text(text = unit, fontSize = unitSize, color = textColor)
            }
        }


    }
}