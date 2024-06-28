package com.currencyconverter.features.screens.root.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.currencyconverter.features.screens.navigation.CurrencyConvertorNavHost
import com.currencyconverter.features.screens.root.mvi.MainViewModel
import com.currencyconverter.features.theme.CurrencyConverterTheme
import com.currencyconverter.utils.collectAsStateLifecycleAware
import com.currencyconverter.utils.observeWithLifecycle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            CurrencyConverterTheme {
                val state by viewModel.uiState.collectAsStateLifecycleAware()
                val navController = rememberNavController()

                viewModel.effect.observeWithLifecycle { label ->
                    when (label) {
                        else -> {}
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CurrencyConvertorNavHost(
                        modifier = Modifier
                            .fillMaxSize(),
                        navController = navController,
                    )
                }
            }
        }
    }
}
