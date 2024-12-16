package com.example.nearby.ui.screen.market

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nearby.cor.network.RemoteDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MarketDetailsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MarketDetailsUiState())
    val uiState: StateFlow<MarketDetailsUiState> = _uiState.asStateFlow()

    fun onEvent(event: MarketDetailsUiEvent) {
        when (event) {
            is MarketDetailsUiEvent.OnFetchCoupons -> coupon(qrCodeContent = event.qrCodeContent)
            is MarketDetailsUiEvent.OnFetchRules -> rules(marketId = event.marketId)
            MarketDetailsUiEvent.OnResetCoupons -> resetCoupon()
        }
    }

    private fun coupon(qrCodeContent: String) {
        viewModelScope.launch {
            RemoteDataSource.patchCoupon(marketId = qrCodeContent).fold(
                onSuccess = { c ->
                    _uiState.update { currentUiState ->
                        currentUiState.copy(
                            coupons = c.coupon
                        )
                    }
                },
                onFailure = {
                    _uiState.update { c ->
                        c.copy(
                            coupons = ""
                        )
                    }
                }
            )
        }
    }

    private fun rules(marketId: String) {
        viewModelScope.launch {
            RemoteDataSource.getMarketDetails(marketId = marketId).fold(
                onSuccess = { m ->
                    _uiState.update { currentUiState ->
                        currentUiState.copy(
                            rules = m.rules
                        )
                    }
                },
                onFailure = {
                    _uiState.update { c ->
                        c.copy(
                            rules = emptyList()
                        )
                    }
                }
            )
        }
    }

    private fun resetCoupon() {
        _uiState.update { c ->
            c.copy(
                coupons = null
            )
        }
    }
}