package com.example.eventiz.ui.components

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eventiz.R
import com.example.eventiz.model.EmergencyType
import com.example.eventiz.ui.screens.EventizViewModel
import com.google.android.gms.location.LocationServices


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@RequiresPermission(
    anyOf = [Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION],
)
@Composable
fun EmergencyTypeButton(usePreciseLocation: Boolean, eventizViewModel: EventizViewModel, emergencyType: EmergencyType) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val locationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    val emergencyColor = Color(0xFC832828)

    // set the image bases on the emergencyType
    val image = when (emergencyType) {
        EmergencyType.FIRE -> R.drawable.fire
        EmergencyType.MEDICAL -> R.drawable.medical
        EmergencyType.OTHER -> R.drawable.other
        EmergencyType.CANCEL -> R.drawable.cancel
        else -> R.drawable.violence
    }

    Column(modifier = Modifier
        .width(75.dp)
        .height(75.dp)
        .background(
            emergencyColor
        )
        .padding(start = 5.dp, top = 10.dp, bottom = 10.dp, end = 5.dp)
        .clickable {
            eventizViewModel.handleEmergency(
                usePreciseLocation,
                scope,
                locationClient,
                emergencyType.type
            )
        },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = painterResource(id = image),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(35.dp)
                .height(35.dp)
        )
        Text(
            text = emergencyType.name,
            color = Color.White,
            fontSize = 14.sp,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 5.dp),
            textAlign = TextAlign.Center,

        )
    }

}