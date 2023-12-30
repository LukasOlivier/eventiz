package com.example.eventiz.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eventiz.ui.screens.EventizViewModel


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun EmergencyButton(eventizViewModel: EventizViewModel, text: String) {
    val emergencyColor = Color(0xFC832828)
    Button(
        modifier = Modifier
            .fillMaxSize()
            .background(emergencyColor)
            .clip(shape = RoundedCornerShape(15.dp)),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.White
        ),
        onClick = {
            eventizViewModel.changeEmergencyState()
        }
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 30.sp,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp),
            textAlign = TextAlign.Center
        )
    }
}