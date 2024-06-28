package com.currencyconverter.features.base.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.currencyconverter.R
import com.currencyconverter.features.theme.Black
import com.currencyconverter.features.theme.Typography

@Composable
fun ItemError(
    onRetryClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = dimensionResource(id = R.dimen.default_big_padding)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.loading_error),
            style = Typography.bodyMedium.copy(
                color = Black,
                textAlign = TextAlign.Center
            )
        )

        DefaultSmallButton(
            modifier = Modifier.padding(top = dimensionResource(id = R.dimen.default_middle_padding)),
            onClick = onRetryClick,
            buttonText = R.string.retry
        )
    }
}