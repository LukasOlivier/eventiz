package com.example.eventiz.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eventiz.ui.screens.EventizUiState

@Composable
fun AnnouncementComponent(uiState: EventizUiState){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
    ) {
        Text(text = "Announcements", modifier = Modifier.padding(start = 10.dp, top = 10.dp))
        Text(
            text = uiState.announcementMessage,
            fontSize = 20.sp,
            lineHeight = 20.sp,
            textAlign = TextAlign.Left,
            modifier = Modifier
                .padding(bottom = 10.dp, top = 20.dp, start = 10.dp, end = 10.dp),

            style = MaterialTheme.typography.labelSmall
        )
    }
}