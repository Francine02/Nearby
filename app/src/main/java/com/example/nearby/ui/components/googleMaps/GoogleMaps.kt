package com.example.nearby.ui.components.googleMaps

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import com.example.nearby.R
import com.example.nearby.data.model.mock.mockUserLocation
import com.example.nearby.ui.screen.home.HomeUIState
import com.example.nearby.ui.util.findNortheast
import com.example.nearby.ui.util.findSouthwest
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.MapProperties
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.internal.toImmutableList
import java.util.Calendar
import kotlin.collections.orEmpty
import kotlin.math.roundToInt

@Composable
fun GoogleMaps(modifier: Modifier = Modifier, uiState: HomeUIState) {
    val contextMap = LocalContext.current

    val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val isNight = currentHour in 18..23 || currentHour in 0..5

    val mapStyle = if (isNight) {
        MapStyleOptions.loadRawResourceStyle(contextMap, R.raw.map_dark)
    } else {
        MapStyleOptions.loadRawResourceStyle(contextMap, R.raw.map_light)
    }

    val cameraPosition = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(mockUserLocation, 13f)
    }
    val uiSettings by remember {
        mutableStateOf(MapUiSettings(zoomControlsEnabled = true))
    }
    val context = LocalContext.current
    val corroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current

    GoogleMap(
        modifier = modifier.fillMaxSize(),
        cameraPositionState = cameraPosition,
        uiSettings = uiSettings,
        properties = MapProperties(mapStyleOptions = mapStyle)
    ) {
        context.getDrawable(R.drawable.ic_user_location)?.let {
            Marker(
                state = MarkerState(position = mockUserLocation),
                icon = BitmapDescriptorFactory.fromBitmap(
                    it.toBitmap(
                        width = density.run { 72.dp.toPx() }.roundToInt(),
                        height = density.run { 72.dp.toPx() }.roundToInt()
                    )
                )
            )
        }
        if (!uiState.markets.isNullOrEmpty()) {
            context.getDrawable(R.drawable.img_pin)?.let {
                uiState.marketLocations?.toImmutableList()
                    ?.forEachIndexed { index, l ->
                        Marker(
                            state = MarkerState(position = l),
                            icon = BitmapDescriptorFactory.fromBitmap(
                                it.toBitmap(
                                    width = density.run { 36.dp.toPx() }
                                        .roundToInt(),
                                    height = density.run { 36.dp.toPx() }
                                        .roundToInt()
                                )
                            ),
                            title = uiState.markets[index].name
                        )
                    }.also {
                        corroutineScope.launch {
                            val allMarks = uiState.marketLocations?.plus(
                                mockUserLocation
                            )

                            val southwest =
                                findSouthwest(points = allMarks.orEmpty())
                            val northeast =
                                findNortheast(points = allMarks.orEmpty())

                            val cameraUpdate =
                                CameraUpdateFactory.newCameraPosition(
                                    CameraPosition(
                                        LatLng(
                                            (southwest.latitude + northeast.latitude) / 2,
                                            (southwest.longitude + northeast.longitude) / 2,
                                        ),
                                        13f,
                                        0f,
                                        0f
                                    )
                                )
                            delay(200)
                            cameraPosition.animate(cameraUpdate, durationMs = 500)
                        }
                    }
            }
        }
    }
}