package com.example.nearby.ui.screen.market

sealed class MarketDetailsUiEvent {
    data class OnFetchRules(val marketId: String) : MarketDetailsUiEvent()
    data class OnFetchCoupons(val qrCodeContent: String) : MarketDetailsUiEvent()
    data object OnResetCoupons : MarketDetailsUiEvent()
}