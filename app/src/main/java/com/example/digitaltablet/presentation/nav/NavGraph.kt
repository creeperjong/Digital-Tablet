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
import com.example.digitaltablet.presentation.startup.QrCodeScannerScreen
import com.example.digitaltablet.presentation.startup.StartUpScreen
import com.example.digitaltablet.presentation.startup.StartUpState
import com.example.digitaltablet.presentation.startup.StartUpViewModel

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val startUpViewModel: StartUpViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = Route.StartUpScreen.route) {
        composable(route = Route.StartUpScreen.route) {
            val state by startUpViewModel.state.collectAsState()
            StartUpScreen(
                state = state,
                onEvent = startUpViewModel::onEvent,
                navigateToRobot = { connectInfo ->
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set("connectInfo", connectInfo)
                    navController.navigate(Route.RobotScreen.route)
                }
            )
        }
        composable(route = Route.QrCodeScannerScreen.route) {
            QrCodeScannerScreen(
                onEvent = startUpViewModel::onEvent,
                navigateUp = { navController.popBackStack() }
            )
        }
        composable(route = Route.RobotScreen.route) {
            val tabletViewModel: TabletViewModel = hiltViewModel()
            val state by tabletViewModel.state.collectAsState()

            navController.previousBackStackEntry
                ?.savedStateHandle
                ?.get<StartUpState>("connectInfo")
                .let { connectInfo ->
                    tabletViewModel.onEvent(
                        TabletEvent.SetConnectInfos(
                            deviceId = connectInfo?.deviceId ?: "",
                            apiKey = connectInfo?.projApiKey ?: "",
                            asstId = connectInfo?.asstId ?: ""
                        )
                    )
                }

            TabletScreen(
                state = state,
                onEvent = tabletViewModel::onEvent
            )
        }
    }
}