package com.example.nearby.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nearby.cor.network.RemoteDataSource
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUIState())
    val uiState: StateFlow<HomeUIState> = _uiState.asStateFlow()

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            HomeUiEvent.OnFetchCategories -> categories()
            is HomeUiEvent.OnFetchMarkets -> markets(categoryId = event.categoryId)
        }
    }

    private fun categories() {
        viewModelScope.launch {
            _uiState.update {
                RemoteDataSource.getCategories().fold(
                    onSuccess = { c ->
                        it.copy(
                            categories = c
                        )
                    },
                    onFailure = { _ ->
                        it.copy(
                            categories = emptyList()
                        )
                    }
                )
            }
        }
    }

    private fun markets(categoryId: String) {
        viewModelScope.launch {
            _uiState.update {
                RemoteDataSource.getMarkets(categoryId = categoryId).fold(
                    onSuccess = { m ->
                        it.copy(
                            markets = m,
                            marketLocations = m.map { market ->
                                LatLng(market.latitude, market.longitude)
                            }
                        )
                    },
                    onFailure = { _ ->
                        it.copy(
                            markets = emptyList(),
                            marketLocations = emptyList()
                        )
                    }
                )
            }
        }
    }
}