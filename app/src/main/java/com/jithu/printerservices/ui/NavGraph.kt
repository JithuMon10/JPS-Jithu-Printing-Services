package com.jithu.printerservices.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun JvsAppNavHost(
    startDestination: String = "pin",
    navController: NavHostController = rememberNavController(),
    isPinSet: Boolean,
    onPinResolved: () -> Unit,
    modifier: Modifier = Modifier,
) {
    NavHost(navController = navController, startDestination = startDestination, modifier = modifier) {
        composable("pin") { /* PinScreen shown via main activity */ }
        composable("dashboard") { /* TODO: Dashboard screen */ }
        composable("orders") { /* TODO: Order list screen */ }
        composable("orderDetail/{orderId}") { /* TODO: Order details/edit screen */ }
        composable("settings") { /* TODO: Settings screen */ }
    }
}
