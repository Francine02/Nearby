package com.example.nearby.ui.screen.home

import com.example.nearby.data.model.Category
import com.example.nearby.data.model.Market
import com.google.android.gms.maps.model.LatLng

data class HomeUIState(
    val categories: List<Category>? = null,
    val markets: List<Market>? = null,
    val marketLocations: List<LatLng>? = null
)
