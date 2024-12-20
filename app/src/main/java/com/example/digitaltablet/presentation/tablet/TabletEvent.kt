package com.example.digitaltablet.presentation.tablet

import android.app.ActionBar.Tab
import android.content.Context
import android.net.Uri
import android.util.Size
import androidx.compose.ui.geometry.Offset
import com.google.common.collect.Table
import java.io.File

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

    data class UploadImage(val image: File?, val onUpload: (File) -> Unit = {}): TabletEvent()

    data object ConfirmDialog: TabletEvent()

    data object ShowDialog: TabletEvent()

    data object DismissDialog: TabletEvent()

    data class ChangeDialogTextInput(val text: String): TabletEvent()

    data class UploadFile(val file: File?): TabletEvent()

    data class ReceiveQrCodeResult(val result: String): TabletEvent()

    data object NavigateUp: TabletEvent()

    data class ChangeCanvasRatio(val ratio: Float): TabletEvent()

    data class SubmitCanvas(val context: Context): TabletEvent()

    data object ClearPlayerCommand: TabletEvent()
}