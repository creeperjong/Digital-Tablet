package com.example.digitaltablet.presentation.tablet

import androidx.compose.ui.geometry.Offset

data class TabletState(

    // UI
    val toastMessages: List<String> = emptyList(),
    val displayTouchArea: Boolean = false,
    val canvasTapPositions: List<Offset> = emptyList(),
    val isCanvasTappable: Boolean = true,
    val canvasWidth: Int = 0,
    val canvasHeight: Int = 0,
    val isCaptionVisible: Boolean = true,
    val isImageVisible: Boolean = false,
    val showTextDialog: Boolean = false,
    val dialogTextInput: String = "",
    val displayOn: Boolean = true,
    val keepContentOn: Boolean = true,

    // R&T
    val deviceId: String = "",
    val caption: String = "",
    val responseCaption: String = "",
    val mediaSources: List<String> = emptyList(),
    val mediaIdx: Int? = null,
    val remoteAccepted: Boolean = false,

    // LLM
    val gptApiKey: String = "",
    val assistantId: String = ""
)