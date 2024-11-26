package com.example.digitaltablet.presentation.tablet

import androidx.compose.ui.geometry.Offset

data class TabletState(

    // UI
    val toastMessages: List<String> = emptyList(),
    val displayTouchArea: Boolean = false,
    val canvasTapPositions: List<Offset> = emptyList(),
    val isCanvasTappable: Boolean = true,
    val isCaptionVisible: Boolean = true,
    val isImageVisible: Boolean = true,
    val showTextDialog: Boolean = false,
    val dialogTextInput: String = "",

    // R&T
    val deviceId: String = "",
    val caption: String = "",
    val responseCaption: String = "",
    val imageSources: List<String> = listOf("https://zidian.18dao.net/image/%E6%9D%AF.png", "https://zidian.18dao.net/image/%E7%93%B6.png", ),
    val imageIdx: Int? = 0,

    // LLM
    val gptApiKey: String = "",
    val assistantId: String = ""
)