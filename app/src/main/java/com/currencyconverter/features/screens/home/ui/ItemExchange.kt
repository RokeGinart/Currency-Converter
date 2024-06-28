package com.currencyconverter.features.screens.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import com.currencyconverter.R
import com.currencyconverter.data.model.ui.ExchangeModel
import com.currencyconverter.data.model.ui.ExchangeType
import com.currencyconverter.features.theme.Black
import com.currencyconverter.features.theme.Gray60
import com.currencyconverter.features.theme.Green
import com.currencyconverter.features.theme.Red
import com.currencyconverter.features.theme.Typography
import com.currencyconverter.features.theme.White

@Composable
fun ItemExchange(
    modifier: Modifier = Modifier,
    exchangeModel: ExchangeModel,
    onValueChange: ((String) -> Unit)? = null,
    onCurrencyClick: () -> Unit,
) {
    var textFieldValueState by remember { mutableStateOf(TextFieldValue(text = exchangeModel.amount)) }

    val color: Color
    val text: String
    val amountColor: Color
    val icon: Int
    val prefix: String
    val enabled: Boolean

    when (exchangeModel.exchangeType) {
        ExchangeType.SELL -> {
            color = Red
            text = stringResource(id = R.string.sell)
            icon = R.drawable.ic_arrow_up
            amountColor = Black
            prefix = ""
            enabled = true
        }

        ExchangeType.RECEIVE -> {
            color = Green
            text = stringResource(id = R.string.receive)
            icon = R.drawable.ic_arrow_down
            amountColor = Green
            prefix = "+"
            enabled = false
        }
    }

    LaunchedEffect(exchangeModel.amount) {
        textFieldValueState = TextFieldValue(
            text = "$prefix${exchangeModel.amount}", selection = when {
                exchangeModel.amount.isEmpty() -> TextRange.Zero
                else -> TextRange(exchangeModel.amount.length)
            }
        )
    }

    Row(
        modifier = Modifier
            .then(modifier)
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.default_padding)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.background(color = color, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier.padding(dimensionResource(id = R.dimen.default_middle_padding)),
                painter = painterResource(id = icon),
                contentDescription = "exchange_icon",
                colorFilter = ColorFilter.tint(White)
            )
        }

        Text(
            modifier = Modifier
                .padding(start = dimensionResource(id = R.dimen.default_middle_padding)),
            text = text,
            style = Typography.bodyMedium.copy(
                color = Black,
                fontWeight = FontWeight(600)
            )
        )

        OutlinedTextField(
            modifier = Modifier
                .weight(3f),
            value = textFieldValueState,
            placeholder = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.sell_hint),
                    style = Typography.bodyMedium.copy(
                        color = Gray60,
                        textAlign = TextAlign.End
                    )
                )
            },
            onValueChange = { newValue ->
                onValueChange?.let { change ->
                    change(newValue.text)
                }
            },
            singleLine = true,
            textStyle = Typography.bodyMedium.copy(
                color = amountColor,
                textAlign = TextAlign.End
            ),

            enabled = enabled,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                disabledBorderColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Done
            ),
        )

        Row(
            modifier = Modifier
                .weight(1f)
                .clip(CircleShape)
                .clickable {
                    onCurrencyClick()
                },
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier,
                text = exchangeModel.currency,
                style = Typography.bodyMedium.copy(
                    color = Black,
                    fontWeight = FontWeight(600)
                )
            )

            Image(
                painter = painterResource(id = R.drawable.ic_small_arrow_down),
                contentDescription = "currency_select_icon"
            )
        }
    }
}