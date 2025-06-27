package com.example.geopet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.MarkerState

@Composable
fun CustomMapMarker(
    imageUrl: String,
    fullName: String,
    location: LatLng,
    onClick: () -> Unit
) {
    val markerState = remember(location) { MarkerState(position = location) }
    val shape = RoundedCornerShape(8.dp, 8.dp, 8.dp, 0.dp)
    var expandMarker by remember { mutableStateOf(false) }

    val painter = rememberAsyncImagePainter(model = imageUrl)

    MarkerComposable(
        keys = arrayOf(fullName, imageUrl, expandMarker),
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
            if (!expandMarker) {
                Image(
                    painter = painter,
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Column(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painter,
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
        }
    }
}
