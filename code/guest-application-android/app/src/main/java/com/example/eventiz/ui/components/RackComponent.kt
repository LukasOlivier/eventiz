package com.example.eventiz.ui.components

import android.nfc.NfcAdapter
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eventiz.ui.screens.EventizUiState
import com.example.eventiz.ui.screens.EventizViewModel

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun RackComponent(
    uiState: EventizUiState,
    viewModel: EventizViewModel,
    onResetClick: () -> Unit,
) {
    val context = LocalContext.current
    val nfcEnabled = NfcAdapter.getDefaultAdapter(context)?.isEnabled
    val onClick : () -> Unit = {
        if (nfcEnabled == false) {
            viewModel.setRackNumber("Tap to enable NFC")
            val enableNfcIntent = android.content.Intent(android.provider.Settings.ACTION_NFC_SETTINGS)
            enableNfcIntent.flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(enableNfcIntent)
        }else{
            viewModel.setRackNumber("Scan bracelet to see rack number")
        }
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clickable {
                onClick()
            }
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Rack number",
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 10.dp, top = 10.dp)
            )
            Text(
                text = "Reset",
                fontSize = 15.sp,
                modifier = Modifier
                    .padding(end = 10.dp, top = 10.dp)
                    .clickable {
                        onResetClick()
                        viewModel.resetRackNumber()
                    }
            )
        }

        Column(
            Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
            var rackFontSize = 20.sp
            if (uiState.rackText != viewModel.defaultRackText && uiState.rackText != viewModel.noNfcRackText && uiState.rackText != viewModel.enableNfcText) {
                rackFontSize = 60.sp
            }
            uiState.rackText?.let {
                Text(
                    text = it,
                    fontSize = rackFontSize,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(bottom = 10.dp),
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }

    }
}