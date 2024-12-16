package com.example.nearby.ui.screen.market

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.nearby.data.model.Market
import com.example.nearby.data.model.mock.mockMarket
import com.example.nearby.ui.components.button.Button
import com.example.nearby.ui.components.market_details.MarketDetailInfo
import com.example.nearby.ui.components.market_details.MarketDetailsCoupons
import com.example.nearby.ui.components.market_details.MarketDetailsRules
import com.example.nearby.ui.theme.Typography
import com.example.nearby.R

@Composable
fun MarketDetailsScreen(
    modifier: Modifier = Modifier,
    uiState: MarketDetailsUiState,
    onEvent: (MarketDetailsUiEvent) -> Unit,
    onNavigateToQRCodeScanner: () -> Unit,
    market: Market, onNavigateBack: () -> Unit
) {
    LaunchedEffect(true) {
        onEvent(MarketDetailsUiEvent.OnFetchRules(marketId = market.id))
    }
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.3f),
            contentDescription = "Imagem do local",
            contentScale = ContentScale.Crop,
            model = market.cover
        )

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp))
                .fillMaxWidth()
                .fillMaxHeight(0.75f)
                .align(Alignment.BottomCenter)
                .background(MaterialTheme.colorScheme.onPrimary)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(36.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    Column {
                        Text(text = market.name, style = Typography.headlineLarge)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = market.description, style = Typography.bodyLarge)
                    }

                    Spacer(modifier = Modifier.height(48.dp))
                    MarketDetailInfo(market = market)
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp)
                    )

                    if (!uiState.rules.isNullOrEmpty())
                        MarketDetailsRules(rule = uiState.rules)
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp)
                    )
                    if (!uiState.coupons.isNullOrEmpty())
                        MarketDetailsCoupons(coupons = listOf(uiState.coupons))
                }
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    text = "Ler QR Code",
                    onClick = onNavigateToQRCodeScanner
                )
            }
        }
        Button(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(24.dp),
            iconRes = R.drawable.ic_arrow_left,
            onClick = onNavigateBack
        )
    }

}

@Preview
@Composable
private fun MarketDetailsScreenPreview() {
    MarketDetailsScreen(
        market = mockMarket.first(),
        uiState = MarketDetailsUiState(),
        onEvent = {},
        onNavigateToQRCodeScanner = {},
        onNavigateBack = {}
    )
}