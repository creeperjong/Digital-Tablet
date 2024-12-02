package com.example.digitaltablet.presentation.tablet

import androidx.compose.ui.geometry.Offset

data class TabletState(

    // UI
    val toastMessages: List<String> = emptyList(),
    val displayTouchArea: Boolean = false,
    val canvasTapPositions: List<Offset> = emptyList(),
    val isCanvasTappable: Boolean = false,
    val isCaptionVisible: Boolean = true,
    val isImageVisible: Boolean = true,
    val showTextDialog: Boolean = false,
    val dialogTextInput: String = "",

    // R&T
    val deviceId: String = "",
    val caption: String = "",
    val responseCaption: String = "",
    val mediaSources: List<String> = emptyList(),
    val mediaIdx: Int? = null,

    // LLM
    val gptApiKey: String = "",
    val assistantId: String = ""
)