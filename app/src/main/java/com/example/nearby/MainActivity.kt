package com.example.nearby

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.nearby.data.model.Market
import com.example.nearby.ui.screen.home.HomeScreen
import com.example.nearby.ui.screen.market.MarketDetailsScreen
import com.example.nearby.ui.screen.SplashScreen
import com.example.nearby.ui.screen.WelcomeScreen
import com.example.nearby.ui.screen.home.HomeViewModel
import com.example.nearby.ui.screen.market.MarketDetailsViewModel
import com.example.nearby.ui.route.Home
import com.example.nearby.ui.route.QRCodeScanner
import com.example.nearby.ui.route.Splash
import com.example.nearby.ui.route.Welcome
import com.example.nearby.ui.screen.market.MarketDetailsUiEvent
import com.example.nearby.ui.screen.qrcode.QRCodeScannerScreen
import com.example.nearby.ui.theme.NearbyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NearbyTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    val homeViewModel by viewModels<HomeViewModel>()
                    val homeUiState by homeViewModel.uiState.collectAsStateWithLifecycle()

                    val marketDetailsViewModel by viewModels<MarketDetailsViewModel>()
                    val marketDetailsUiState by marketDetailsViewModel.uiState.collectAsStateWithLifecycle()

                    NavHost(
                        navController = navController,
                        startDestination = Splash
                    ) {
                        composable<Splash> {
                            SplashScreen(
                                onNavigateToWelcome = {
                                    navController.navigate(Welcome)
                                }
                            )
                        }
                        composable<Welcome> {
                            WelcomeScreen(
                                onNavigateToHome = {
                                    navController.navigate(Home)
                                }
                            )
                        }
                        composable<Home> {
                            HomeScreen(
                                onNavigateToMarketDetails = { selectedMarket ->
                                    navController.navigate(selectedMarket)
                                },
                                uiState = homeUiState,
                                onEvent = homeViewModel::onEvent
                            )
                        }
                        composable<Market> {
                            val selectedMarket = it.toRoute<Market>()

                            MarketDetailsScreen(
                                market = selectedMarket,
                                uiState = marketDetailsUiState,
                                onEvent = marketDetailsViewModel::onEvent,
                                onNavigateBack = {
                                    navController.popBackStack()
                                },
                                onNavigateToQRCodeScanner = {
                                    navController.navigate(QRCodeScanner)
                                }
                            )
                        }
                        composable<QRCodeScanner> {
                            QRCodeScannerScreen(
                                onCompletedScan = { qr ->
                                    if (qr.isNotEmpty()) {
                                        marketDetailsViewModel.onEvent(
                                            MarketDetailsUiEvent.OnFetchCoupons(
                                                qr
                                            )
                                        )
                                        navController.popBackStack()
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}