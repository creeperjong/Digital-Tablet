package com.example.digitaltablet.presentation.nav

sealed class Route(
    val route: String
) {
    data object StartUpScreen: Route(route = "StartUpScreen")
    data object QrCodeScannerScreen: Route(route = "QrCodeScannerScreen")
    data object TabletScreen: Route(route = "TabletScreen")
}