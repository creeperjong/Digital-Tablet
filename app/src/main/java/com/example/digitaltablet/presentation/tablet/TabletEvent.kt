package com.example.digitaltablet.presentation.tablet

import android.net.Uri
import androidx.compose.ui.geometry.Offset

sealed class TabletEvent {

    data object ClearToastMsg: TabletEvent()

    data class SetConnectInfos(
        val deviceId: String,
        val apiKey: String,
        val asstId: String,
    ): TabletEvent()

    data object ConnectMqttBroker: TabletEvent()

    data object DisconnectMqttBroker: TabletEvent()

    data class TapOnCanvas(val position: Offset): TabletEvent()

    data object ClearCanvas: TabletEvent()

    data object ToggleCaptionVisibility: TabletEvent()

    data object ToggleImageVisibility: TabletEvent()

    data class SwitchImage(val page: Int): TabletEvent()

    data class UploadImage(val uri: Uri?, val onSent: (Uri) -> Unit = {}): TabletEvent()

    data object ConfirmDialog: TabletEvent()

    data object ShowDialog: TabletEvent()

    data object DismissDialog: TabletEvent()

    data class ChangeDialogTextInput(val text: String): TabletEvent()

}