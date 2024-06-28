package com.currencyconverter.features.base.compose

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.currencyconverter.R
import com.currencyconverter.features.theme.Black
import com.currencyconverter.features.theme.Blue
import com.currencyconverter.features.theme.Border
import com.currencyconverter.features.theme.Gray30
import com.currencyconverter.features.theme.Typography
import com.currencyconverter.features.theme.White
import kotlinx.coroutines.delay

val defaultShape = RoundedCornerShape(10.dp)

@Composable
fun DefaultHorizontalDivider(
    modifier: Modifier = Modifier,
    color: Color = Border,
) {
    HorizontalDivider(modifier = modifier, color = color)
}

@Composable
fun KeyboardAwareScreen(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier
            .then(modifier)
            .imePadding()
    ) {
        content()
    }
}


@Composable
fun DefaultButton(
    modifier: Modifier = Modifier,
    text: Int,
    @DrawableRes icon: Int? = null,
    isEnabled: Boolean = true,
    onClick: () -> Unit,
) {
    var enableAgain by remember { mutableStateOf(true) }
    LaunchedEffect(enableAgain, block = {
        if (enableAgain) return@LaunchedEffect
        delay(timeMillis = 500L)
        enableAgain = true
    })

    Button(
        modifier = Modifier
            .then(modifier)
            .height(56.dp),
        shape = RoundedCornerShape(25.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Blue,
            contentColor = White,
        ),
        enabled = isEnabled,
        onClick = {
            if (enableAgain) {
                enableAgain = false
                onClick()
            }
        }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.default_above_middle_padding)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon?.let {
                Image(
                    modifier = Modifier.padding(end = dimensionResource(id = R.dimen.default_middle_padding)),
                    painter = painterResource(id = it),
                    contentDescription = "black_button_icon"
                )
            }
            Text(
                text = stringResource(id = text),
                style = Typography.bodyMedium.copy(color = White)
            )
        }
    }
}


@Composable
fun DefaultOutlineButton(
    modifier: Modifier = Modifier,
    text: Int,
    @DrawableRes icon: Int? = null,
    isEnabled: Boolean = true,
    onClick: () -> Unit,
) {
    var enableAgain by remember { mutableStateOf(true) }
    LaunchedEffect(enableAgain, block = {
        if (enableAgain) return@LaunchedEffect
        delay(timeMillis = 500L)
        enableAgain = true
    })

    Button(
        modifier = Modifier.then(modifier),
        shape = RoundedCornerShape(25.dp),
        border = BorderStroke(1.dp, Black),
        enabled = isEnabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = White,
        ),
        onClick = {
            if (enableAgain) {
                enableAgain = false
                onClick()
            }
        }
    ) {
        Row(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.default_above_middle_padding)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon?.let {
                Image(
                    modifier = Modifier.padding(end = dimensionResource(id = R.dimen.default_middle_padding)),
                    painter = painterResource(id = it),
                    contentDescription = "black_button_icon"
                )
            }
            Text(
                text = stringResource(id = text),
                style = Typography.bodyMedium
            )
        }
    }
}

@Composable
fun DefaultSmallButton(
    modifier: Modifier = Modifier,
    buttonText: Int,
    onClick: () -> Unit,
) {
    var enableAgain by remember { mutableStateOf(true) }
    LaunchedEffect(enableAgain, block = {
        if (enableAgain) return@LaunchedEffect
        delay(timeMillis = 500L)
        enableAgain = true
    })

    Button(
        modifier = Modifier
            .then(modifier)
            .defaultMinSize(minHeight = 1.dp, minWidth = 1.dp),
        contentPadding = PaddingValues(0.dp),
        shape = RoundedCornerShape(25.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Blue,
            contentColor = White,
        ),
        onClick = {
            if (enableAgain) {
                enableAgain = false
                onClick()
            }
        }
    ) {
        Text(
            modifier = Modifier.padding(
                dimensionResource(id = R.dimen.default_middle_padding),
            ),
            text = stringResource(id = buttonText),
            style = Typography.bodyMedium.copy(
                color = White,
                fontWeight = FontWeight(500),
            )
        )
    }
}

@Composable
fun DefaultLoader(
    modifier: Modifier = Modifier,
    trackColor: Color = Gray30,
    strokeWidth: Dp = 4.dp,
) {
    Box(modifier = Modifier.then(modifier), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            modifier = Modifier.size(40.dp),
            color = Blue,
            trackColor = trackColor,
            strokeWidth = strokeWidth
        )
    }
}

@Composable
fun keyboardAsState(): State<Boolean> {
    val isImeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    return rememberUpdatedState(isImeVisible)
}