package com.example.nearby.ui.screen.market

import com.example.nearby.data.model.Rule

data class MarketDetailsUiState(
    val rules: List<Rule>? = null,
    val coupons: String? = null
)
