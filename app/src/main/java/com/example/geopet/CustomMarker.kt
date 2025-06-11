package com.example.geopet

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.MarkerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue


@Composable
fun CustomMapMarker(
    @DrawableRes imageResId: Int?, // Puede ser nulo para mostrar solo texto
    fullName: String,
    location: LatLng,
    onClick: () -> Unit
) {
    val markerState = remember(location) { MarkerState(position = location) } // recomputa cuando location cambia
    val shape = RoundedCornerShape(8.dp, 8.dp, 8.dp, 0.dp)

    var expandMarker by remember { mutableStateOf(false) }

    MarkerComposable(
        keys = arrayOf(fullName, imageResId ?: -1, expandMarker),
        state = markerState,
        title = fullName,
        anchor = Offset(0.5f, 1f),
        onClick = {
            onClick()
            expandMarker = !expandMarker
            true
        }
    ) {
        Box(
            modifier = Modifier
                .size(if (expandMarker) 100.dp else 30.dp)
                .clip(shape)
                .background(Color.White)
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            if (imageResId != null) {
                if (!expandMarker) {
                    Image(
                        painter = painterResource(id = imageResId),
                        contentDescription = "Profile Image",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Column(modifier = Modifier.fillMaxSize()) {
                        Image(
                            painter = painterResource(id = imageResId),
                            contentDescription = "Profile Image",
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Text(
                            text = fullName,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }
            } else {
                Text(
                    text = fullName.take(1).uppercase(),
                    color = Color.White,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}


