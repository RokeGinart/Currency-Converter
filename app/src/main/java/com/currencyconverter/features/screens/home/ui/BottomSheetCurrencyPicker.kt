package com.currencyconverter.features.screens.home.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.currencyconverter.data.model.ui.Currency
import com.currencyconverter.data.model.ui.ExchangeType
import com.currencyconverter.features.screens.currency_picker.ui.CurrencyPickerScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetCurrencyPicker(
    sheetState: SheetState,
    type: ExchangeType,
    onCurrencySelected: (Currency, ExchangeType)-> Unit,
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(
        modifier = Modifier.statusBarsPadding(),
        windowInsets = WindowInsets(top = 0),
        sheetState = sheetState,
        dragHandle = null,
        onDismissRequest = { onDismiss() }
    ) {
        CurrencyPickerScreen(
            type = type,
            onCloseClick = onDismiss,
            onCurrencySelected = onCurrencySelected
        )
    }
}