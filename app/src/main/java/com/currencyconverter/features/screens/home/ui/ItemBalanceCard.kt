package com.currencyconverter.features.screens.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.currencyconverter.data.model.ui.Balance
import com.currencyconverter.features.theme.Orange
import com.currencyconverter.features.theme.Typography
import com.currencyconverter.features.theme.White

@Composable
fun ItemBalanceCard(
    modifier: Modifier,
    balance: Balance,
) {
    Column(
        modifier = Modifier
            .then(modifier)
            .background(color = Orange, shape = RoundedCornerShape(10.dp)),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = balance.toString(),
            style = Typography.bodyLarge.copy(
                color = White,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight(600)
            )
        )
    }
}