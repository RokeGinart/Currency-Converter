package com.currencyconverter.features.screens.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.currencyconverter.features.screens.home.ui.HomeScreen


private const val HOME_SCREEN = "home_screen"

@Composable
fun CurrencyConvertorNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = HOME_SCREEN,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = HOME_SCREEN) {
            HomeScreen()
        }
    }
}
