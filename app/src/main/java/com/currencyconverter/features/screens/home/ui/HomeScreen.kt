package com.currencyconverter.features.screens.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.currencyconverter.R
import com.currencyconverter.features.base.compose.DefaultButton
import com.currencyconverter.features.base.compose.DefaultHorizontalDivider
import com.currencyconverter.features.base.compose.DefaultLoader
import com.currencyconverter.features.base.compose.ItemError
import com.currencyconverter.features.base.compose.KeyboardAwareScreen
import com.currencyconverter.features.base.compose.keyboardAsState
import com.currencyconverter.features.screens.home.mvi.HomeContract
import com.currencyconverter.features.screens.home.mvi.HomeViewModel
import com.currencyconverter.features.theme.Blue
import com.currencyconverter.features.theme.Gray60
import com.currencyconverter.features.theme.Typography
import com.currencyconverter.features.theme.White
import com.currencyconverter.utils.collectAsStateLifecycleAware
import com.currencyconverter.utils.observeWithLifecycle
import com.currencyconverter.utils.showToast
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val scrollState = rememberLazyListState()
    val state by viewModel.uiState.collectAsStateLifecycleAware()
    val keyboardController = LocalSoftwareKeyboardController.current
    val modalBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val coroutineScope = rememberCoroutineScope()

    val focusManager = LocalFocusManager.current
    val isKeyboardOpen by keyboardAsState()
    if (!isKeyboardOpen) focusManager.clearFocus()

    viewModel.effect.observeWithLifecycle { label ->
        when (label) {
            HomeContract.Effect.CloseKeyboard -> keyboardController?.hide()
            is HomeContract.Effect.ShowToast -> showToast(context, label.message)
            HomeContract.Effect.CloseBottomSheet -> coroutineScope.launch {
                modalBottomSheetState.hide()
            }.invokeOnCompletion {
                viewModel.setEvent(HomeContract.Event.OnHideBottomSheet)
            }

            is HomeContract.Effect.ScrollToCard -> coroutineScope.launch {
                scrollState.animateScrollToItem(label.index)
            }
        }
    }

    state.bottomSheet?.let { bottomSheet ->
        when (bottomSheet) {
            is HomeContract.HomeBottomSheets.CurrencyPicker -> BottomSheetCurrencyPicker(
                sheetState = modalBottomSheetState,
                type = bottomSheet.exchangeType,
                onCurrencySelected = { currency, exchangeType ->
                    viewModel.setEvent(
                        HomeContract.Event.OnCurrencySelected(
                            currency,
                            exchangeType
                        )
                    )
                },
                onDismiss = {
                    viewModel.setEvent(HomeContract.Event.OnCloseBottomSheet)
                }
            )
        }
    }

    if (state.showCheckoutDialog) DialogCheckout(
        sellAmount = state.sellExchange.toString(),
        receiveAmount = state.receiveExchange.toString(),
        fee = state.fee.getFee(),
        onConfirm = {
            viewModel.setEvent(HomeContract.Event.OnConfirmCheckoutClick)
        },
        onDismiss = {
            viewModel.setEvent(HomeContract.Event.OnCloseCheckoutClick)
        }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Blue)
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(dimensionResource(id = R.dimen.default_padding)),
                    text = stringResource(id = R.string.app_name),
                    style = Typography.bodyMedium.copy(
                        color = White,
                        textAlign = TextAlign.Center,
                    )
                )
            }
            KeyboardAwareScreen {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = dimensionResource(id = R.dimen.default_padding))
                                .padding(top = dimensionResource(id = R.dimen.default_big_padding)),
                            text = stringResource(id = R.string.balance).uppercase(),
                            style = Typography.bodyMedium.copy(
                                color = Gray60,
                                fontWeight = FontWeight(600)
                            )
                        )

                        LazyRow(
                            modifier = Modifier
                                .padding(top = dimensionResource(id = R.dimen.default_padding))
                                .fillMaxWidth(),
                            state = scrollState,
                            flingBehavior = rememberSnapFlingBehavior(lazyListState = scrollState),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp)
                        ) {
                            items(state.balance) { item ->
                                ItemBalanceCard(
                                    modifier = Modifier
                                        .width(screenWidth - 32.dp)
                                        .aspectRatio(1.9f),
                                    balance = item,
                                )
                            }
                        }

                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = dimensionResource(id = R.dimen.default_padding))
                                .padding(top = dimensionResource(id = R.dimen.default_big_padding)),
                            text = stringResource(id = R.string.exchange).uppercase(),
                            style = Typography.bodyMedium.copy(
                                color = Gray60,
                                fontWeight = FontWeight(600)
                            )
                        )

                        when {
                            state.showError -> ItemError(onRetryClick = {
                                viewModel.setEvent(HomeContract.Event.OnRetryClick)
                            })

                            state.exchangeRateIsLoading -> DefaultLoader(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = dimensionResource(id = R.dimen.default_big_padding))
                            )

                            else -> {
                                ItemExchange(
                                    modifier = Modifier.padding(top = dimensionResource(id = R.dimen.default_big_padding)),
                                    exchangeModel = state.sellExchange,
                                    onValueChange = {
                                        viewModel.setEvent(HomeContract.Event.OnSellAmountChange(it))
                                    },
                                    onCurrencyClick = {
                                        viewModel.setEvent(
                                            HomeContract.Event.OnCurrencyPickerClick(
                                                state.sellExchange.exchangeType
                                            )
                                        )
                                    }
                                )

                                DefaultHorizontalDivider(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            start = 64.dp,
                                            end = dimensionResource(id = R.dimen.default_padding),
                                        )
                                        .padding(
                                            vertical = dimensionResource(id = R.dimen.default_middle_padding),
                                        )
                                )

                                ItemExchange(
                                    exchangeModel = state.receiveExchange,
                                    onCurrencyClick = {
                                        viewModel.setEvent(
                                            HomeContract.Event.OnCurrencyPickerClick(
                                                state.receiveExchange.exchangeType
                                            )
                                        )
                                    }
                                )

                                if (state.fee.isFree) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(
                                                top = dimensionResource(id = R.dimen.default_middle_padding)
                                            )
                                            .padding(
                                                horizontal = dimensionResource(id = R.dimen.default_padding)
                                            ),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.ic_info_circle),
                                            contentDescription = "info_icon",
                                            colorFilter = ColorFilter.tint(Blue)
                                        )

                                        Text(
                                            modifier = Modifier
                                                .weight(1f)
                                                .padding(start = dimensionResource(id = R.dimen.default_smallest_padding)),
                                            text = stringResource(
                                                id = R.string.fee_free,
                                                state.fee.freeTransactionCount
                                            ),
                                            style = Typography.bodyMedium.copy(
                                                color = Blue
                                            )
                                        )
                                    }
                                }

                                Spacer(
                                    modifier =
                                    Modifier.height(dimensionResource(id = R.dimen.bottom_button_padding))
                                )
                            }
                        }
                    }

                    if (state.showExchangeButton)
                        DefaultButton(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth()
                                .navigationBarsPadding()
                                .padding(dimensionResource(id = R.dimen.default_padding)),
                            text = R.string.submit,
                            onClick = {
                                viewModel.setEvent(HomeContract.Event.OnExchangeClick)
                            }
                        )
                }
            }
        }
    }
}