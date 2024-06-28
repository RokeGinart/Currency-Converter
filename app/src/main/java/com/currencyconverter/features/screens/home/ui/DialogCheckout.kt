package com.currencyconverter.features.screens.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import com.currencyconverter.R
import com.currencyconverter.features.base.compose.DefaultButton
import com.currencyconverter.features.base.compose.DefaultOutlineButton
import com.currencyconverter.features.base.compose.defaultShape
import com.currencyconverter.features.theme.Gray60
import com.currencyconverter.features.theme.Typography
import com.currencyconverter.features.theme.White

@Composable
fun DialogCheckout(
    sellAmount: String,
    receiveAmount: String,
    fee: String? = null,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = White, shape = defaultShape)
                .padding(dimensionResource(id = R.dimen.default_padding))
        ) {
            Text(
                modifier = Modifier,
                text = stringResource(id = R.string.checkout_title),
                style = Typography.bodyLarge.copy(
                    fontWeight = FontWeight(600)
                )
            )
            CheckoutItem(
                modifier = Modifier.padding(top = dimensionResource(id = R.dimen.default_big_padding)),
                title = R.string.checkout_sell,
                value = sellAmount
            )
            CheckoutItem(
                modifier = Modifier.padding(top = dimensionResource(id = R.dimen.default_middle_padding)),
                title = R.string.checkout_receive,
                value = receiveAmount
            )
            fee?.let {
                CheckoutItem(
                    modifier = Modifier.padding(top = dimensionResource(id = R.dimen.default_middle_padding)),
                    title = R.string.checkout_fee,
                    value = fee
                )
            }

            DefaultButton(
                modifier = Modifier
                    .padding(top = dimensionResource(id = R.dimen.default_big_padding))
                    .fillMaxWidth(),
                text = R.string.checkout_confirm,
                onClick = onConfirm
            )

            DefaultOutlineButton(
                modifier = Modifier
                    .padding(top = dimensionResource(id = R.dimen.default_middle_padding))
                    .fillMaxWidth(),
                text = R.string.checkout_cancel,
                onClick = onDismiss
            )
        }
    }
}

@Composable
private fun CheckoutItem(
    modifier: Modifier = Modifier,
    title: Int,
    value: String,
) {
    Row(
        modifier = Modifier
            .then(modifier)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = title),
            style = Typography.bodyMedium.copy(color = Gray60)
        )
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(start = dimensionResource(id = R.dimen.default_middle_padding)),
            text = value,
            style = Typography.bodyMedium.copy(
                textAlign = TextAlign.End
            )
        )
    }
}