package com.example.digitaltablet.presentation.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.digitaltablet.presentation.tablet.TabletEvent
import com.example.digitaltablet.presentation.tablet.TabletScreen
import com.example.digitaltablet.presentation.tablet.TabletViewModel
import com.example.digitaltablet.presentation.startup.StartUpScreen
import com.example.digitaltablet.presentation.startup.StartUpState
import com.example.digitaltablet.presentation.startup.StartUpViewModel
import com.example.digitaltablet.presentation.tablet.QrCodeScannerScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val startUpViewModel: StartUpViewModel = hiltViewModel()
    val tabletViewModel: TabletViewModel = hiltViewModel()


    NavHost(navController = navController, startDestination = Route.StartUpScreen.route) {
        composable(route = Route.StartUpScreen.route) {
            val state by startUpViewModel.state.collectAsState()
            StartUpScreen(
                state = state,
                onEvent = startUpViewModel::onEvent,
                navigateToTablet = { connectInfo ->
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set("connectInfo", connectInfo)
                    navController.navigate(Route.TabletScreen.route)
                }
            )
        }
        composable(route = Route.QrCodeScannerScreen.route) {
            QrCodeScannerScreen(
                onResult = { result ->
                    tabletViewModel.onEvent(TabletEvent.ReceiveQrCodeResult(result))
                },
                navigateUp = {
                    navController.popBackStack()
                }
            )
        }
        composable(route = Route.TabletScreen.route) {

            val state by tabletViewModel.state.collectAsState()
            val previousRoute = navController.previousBackStackEntry?.destination?.route

            if (previousRoute == Route.StartUpScreen.route) {
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<StartUpState>("connectInfo")
                    .let { connectInfo ->
                        val deviceId = connectInfo?.deviceId ?: ""
                        val apiKey = connectInfo?.projApiKey ?: ""
                        val asstId = connectInfo?.asstId ?: ""
                        if ((deviceId != state.deviceId) ||
                            (apiKey != state.gptApiKey) ||
                            (asstId != state.assistantId)
                        ){
                            tabletViewModel.onEvent(
                                TabletEvent.SetConnectInfos(
                                    deviceId = connectInfo?.deviceId ?: "",
                                    apiKey = connectInfo?.projApiKey ?: "",
                                    asstId = connectInfo?.asstId ?: ""
                                )
                            )
                        }
                    }
            }

            TabletScreen(
                state = state,
                onEvent = tabletViewModel::onEvent,
                navigateToScanner = { navController.navigate(Route.QrCodeScannerScreen.route) },
                navigateUp = {
                    tabletViewModel.onEvent(TabletEvent.DisconnectMqttBroker)
                    navController.popBackStack()
                }
            )
        }
    }
}