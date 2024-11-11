package com.example.digitaltablet.presentation.robot

data class TabletState(

    // UI
    val toastMessages: List<String> = emptyList(),
    val displayTouchArea: Boolean = false,

    // R&T
    val deviceId: String = "",

)