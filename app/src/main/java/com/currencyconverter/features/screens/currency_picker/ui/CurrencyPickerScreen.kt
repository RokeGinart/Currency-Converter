package com.currencyconverter.features.screens.currency_picker.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.currencyconverter.R
import com.currencyconverter.data.model.ui.Currency
import com.currencyconverter.data.model.ui.ExchangeType
import com.currencyconverter.features.base.compose.DefaultHorizontalDivider
import com.currencyconverter.features.base.compose.DefaultLoader
import com.currencyconverter.features.base.compose.ItemError
import com.currencyconverter.features.base.compose.defaultShape
import com.currencyconverter.features.screens.currency_picker.mvi.CurrencyPickerContract
import com.currencyconverter.features.screens.currency_picker.mvi.CurrencyPickerViewModel
import com.currencyconverter.features.theme.Black
import com.currencyconverter.features.theme.Blue
import com.currencyconverter.features.theme.Border
import com.currencyconverter.features.theme.Gray60
import com.currencyconverter.features.theme.Typography
import com.currencyconverter.features.theme.White
import com.currencyconverter.utils.collectAsStateLifecycleAware
import com.currencyconverter.utils.observeWithLifecycle

@Composable
fun CurrencyPickerScreen(
    type: ExchangeType,
    onCloseClick: () -> Unit,
    onCurrencySelected: (Currency, ExchangeType) -> Unit,
    viewModel: CurrencyPickerViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateLifecycleAware()

    val isSearchFocused = remember {
        mutableStateOf(false)
    }

    viewModel.effect.observeWithLifecycle { label ->
        when (label) {
            CurrencyPickerContract.Effect.Close -> onCloseClick()
            is CurrencyPickerContract.Effect.ReturnResult -> onCurrencySelected(
                label.currency,
                label.exchangeType
            )
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.setEvent(CurrencyPickerContract.Event.InitScreen(type))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .navigationBarsPadding()
            .padding(vertical = dimensionResource(id = R.dimen.default_big_padding))
            .padding(horizontal = dimensionResource(id = R.dimen.default_under_big_padding))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {
                        viewModel.setEvent(CurrencyPickerContract.Event.OnCloseClick)
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.default_smallest_padding)),
                    painter = painterResource(id = R.drawable.ic_close),
                    contentDescription = "search_close_icon"
                )
            }
            Text(
                modifier = Modifier.padding(start = dimensionResource(id = R.dimen.default_middle_padding)),
                text = stringResource(id = R.string.currency_picker),
                style = Typography.bodyMedium
            )
        }
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = dimensionResource(id = R.dimen.default_middle_padding))
                .background(color = White, shape = defaultShape)
                .border(
                    width = 1.dp,
                    color = if (isSearchFocused.value) Blue else Border,
                    shape = defaultShape
                )
                .onFocusChanged {
                    isSearchFocused.value = it.isFocused
                },
            shape = defaultShape,
            value = state.search,
            singleLine = true,
            onValueChange = {
                viewModel.setEvent(CurrencyPickerContract.Event.OnValueChanged(it))
            },
            leadingIcon = remember {
                {
                    Image(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = "search_icon"
                    )
                }
            },
            placeholder = remember {
                {
                    Text(
                        text = stringResource(id = R.string.search_hint),
                        style = Typography.bodyMedium.copy(color = Gray60)
                    )
                }
            },
            textStyle = Typography.bodyMedium,
            colors = TextFieldDefaults.colors(
                focusedTextColor = Black,
                unfocusedTextColor = Black,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedContainerColor = White,
                unfocusedContainerColor = White,
                cursorColor = Black
            )
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = dimensionResource(id = R.dimen.default_middle_padding))
        ) {
            when {
                state.isLoading -> {
                    item {
                        DefaultLoader(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = dimensionResource(id = R.dimen.default_big_padding))
                        )
                    }
                }

                state.showError -> {
                    item {
                        ItemError(onRetryClick = {
                            viewModel.setEvent(CurrencyPickerContract.Event.OnRetryClick)
                        })
                    }
                }

                else -> {
                    if (state.searchResult != null) {
                        state.searchResult?.let { result ->
                            if (result.isEmpty()) {
                                item { EmptyResult() }
                            } else {
                                items(result) { item ->
                                    CurrencyView(
                                        currency = item,
                                        type = state.type,
                                        onCurrencyClick = {
                                            viewModel.setEvent(
                                                CurrencyPickerContract.Event.OnCurrencyClicked(
                                                    item
                                                )
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    } else {
                        items(state.currencies) { item ->
                            CurrencyView(
                                currency = item,
                                type = state.type,
                                onCurrencyClick = {
                                    viewModel.setEvent(
                                        CurrencyPickerContract.Event.OnCurrencyClicked(
                                            item
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CurrencyView(
    currency: Currency,
    type: ExchangeType,
    onCurrencyClick: () -> Unit,
) {
    val text = when (type) {
        ExchangeType.RECEIVE -> stringResource(
            id = R.string.exchange_rate,
            currency.rate.toString()
        )

        ExchangeType.SELL -> stringResource(id = R.string.user_balance_value, currency.rate)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(defaultShape)
            .clickable {
                onCurrencyClick()
            }
            .padding(top = dimensionResource(id = R.dimen.default_padding)),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = currency.currency,
            style = Typography.bodyMedium
        )
        Text(
            text = text,
            style = Typography.bodySmall.copy(color = Gray60)
        )
        DefaultHorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = dimensionResource(
                        id = R.dimen.default_middle_padding
                    )
                )
        )
    }
}

@Composable
private fun EmptyResult() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .padding(top = dimensionResource(id = R.dimen.default_enormous_padding))
                .size(40.dp),
            painter = painterResource(id = R.drawable.ic_empty_search),
            contentDescription = "empty_search_icon"
        )
        Text(
            modifier = Modifier.padding(top = dimensionResource(id = R.dimen.default_under_big_padding)),
            text = stringResource(id = R.string.empty_search_title),
            style = Typography.bodyLarge
        )
        Text(
            modifier = Modifier.padding(top = dimensionResource(id = R.dimen.default_middle_padding)),
            text = stringResource(id = R.string.empty_search_description),
            style = Typography.bodyMedium.copy(color = Gray60),
            textAlign = TextAlign.Center
        )
    }
}