package com.example.digitaltablet.presentation.nav

sealed class Route(
    val route: String,
    val argName: String? = null
) {
    data object StartUpScreen: Route(route = "StartUpScreen")
    data object QrCodeScannerScreen: Route(route = "QrCodeScannerScreen")
    data object RobotScreen: Route(route = "RobotScreen")
}