package com.example.digitaltablet.presentation.tablet

import android.net.Uri
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import com.example.digitaltablet.domain.usecase.MqttUseCase
import com.example.digitaltablet.domain.usecase.RcslUseCase
import com.example.digitaltablet.util.Constants.Mqtt
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min

@HiltViewModel
class TabletViewModel @Inject constructor(
    private val mqttUseCase: MqttUseCase,
    private val rcslUseCase: RcslUseCase,
): ViewModel() {

    private val _state = MutableStateFlow(TabletState())
    val state: StateFlow<TabletState> = _state.asStateFlow()

    fun onEvent(event: TabletEvent) {
        when (event) {
            is TabletEvent.ClearToastMsg -> {
                clearToast()
            }
            is TabletEvent.SetConnectInfos -> {
                setConnectInfos(
                    deviceId = event.deviceId,
                    apiKey = event.apiKey,
                    asstId = event.asstId
                )
            }
            is TabletEvent.ConnectMqttBroker -> {
                connectMqtt()
            }
            is TabletEvent.DisconnectMqttBroker -> {
                disconnectMqtt()
            }
            is TabletEvent.TapOnCanvas -> {
                onCanvasTapped(event.position)
            }
            is TabletEvent.ClearCanvas -> {
                clearCanvas()
            }
            is TabletEvent.ToggleCaptionVisibility -> {
                toggleCaptionVisibility()
            }
            is TabletEvent.ToggleImageVisibility -> {
                toggleImageVisibility()
            }
            is TabletEvent.SwitchImage -> {
                switchImage(event.page)
            }
            is TabletEvent.UploadImage -> {
                sendImage(event.uri, event.onSent)
            }
            is TabletEvent.ConfirmDialog -> {
                sendTextInput(_state.value.dialogTextInput)
                _state.value = _state.value.copy(showTextDialog = false, dialogTextInput = "")
            }
            is TabletEvent.ShowDialog -> {
                _state.value = _state.value.copy(showTextDialog = true)
            }
            is TabletEvent.DismissDialog -> {
                _state.value = _state.value.copy(showTextDialog = false)
            }
            is TabletEvent.ChangeDialogTextInput -> {
                _state.value = _state.value.copy(dialogTextInput = event.text)
            }
            is TabletEvent.UploadFile -> {
                sendFile(event.uri)
            }
            is TabletEvent.ReceiveQrCodeResult -> {
                sendTextInput(event.result)
            }
        }
    }

    private fun setConnectInfos(deviceId: String, apiKey: String, asstId: String) {
        _state.value = _state.value.copy(
            deviceId = deviceId,
            gptApiKey = apiKey,
            assistantId = asstId
        )
    }

    private fun resetAllTempStates() {
        // TODO
    }

    /*
     *  UI related functions
     */

    private fun showToast(message: String) {
        val currentMessages = _state.value.toastMessages.toMutableList()
        currentMessages.add(message)
        _state.value = _state.value.copy(toastMessages = currentMessages)
    }

    private fun clearToast() {
        _state.value = _state.value.copy(toastMessages = emptyList())
    }

    private fun onCanvasTapped(position: Offset) {
        _state.value = _state.value.copy(
            canvasTapPositions = _state.value.canvasTapPositions + position
        )
    }

    private fun clearCanvas() {
        _state.value = _state.value.copy(canvasTapPositions = emptyList())
    }

    private fun toggleCaptionVisibility() {
        _state.value = _state.value.copy(isCaptionVisible = !_state.value.isCaptionVisible)
    }

    private fun toggleImageVisibility() {
        _state.value = _state.value.copy(isImageVisible = !_state.value.isImageVisible)
    }

    private fun switchImage(page: Int) {
        val maxPage = _state.value.mediaSources.size
        _state.value = _state.value.copy(
            mediaIdx = max(min(page, maxPage - 1), 0),
            canvasTapPositions = emptyList()
        )
    }

    /*
     *  R&T related functions
     */

    private fun sendConnectInfos() {
        val apiKey = _state.value.gptApiKey
        val asstId = _state.value.assistantId
        if (apiKey.isNotBlank()) {
            mqttUseCase.publish(
                topic = getFullTopic(Mqtt.Topic.API_KEY),
                message = apiKey,
                qos = 0
            )
        }
        if (asstId.isNotBlank()) {
            mqttUseCase.publish(
                topic = getFullTopic(Mqtt.Topic.ASST_ID),
                message = asstId,
                qos = 0
            )
        }
    }

    private fun sendImage(uri: Uri?, onSent: (Uri) -> Unit) {
        if ( uri == null ) {
            showToast("Error: Image not found.")
        } else {
            // TODO: upload & after sent
        }
    }

    private fun sendTextInput(text: String) {
        mqttUseCase.publish(
            topic = getFullTopic(Mqtt.Topic.TEXT_INPUT),
            message = text,
            qos = 0
        )
    }

    private fun sendFile(uri: Uri?) {
        if ( uri == null) {
            showToast("Error: File not found")
        } else {
            // TODO
        }
    }


    // TODO

    /*
     *  MQTT related functions
     */

    private fun connectMqtt() {
        mqttUseCase.bindService {
            mqttUseCase.connect(
                host = Mqtt.BROKER_URL,
                deviceId = _state.value.deviceId,
                onConnected = {
                    initialSubscription()
                    sendConnectInfos()
                },
                onMessageArrived = { topic, message ->
                    onMqttMessageArrived(topic, message)
                }
            )
        }
    }

    private fun disconnectMqtt() {
        mqttUseCase.disconnect {}
    }

    private fun initialSubscription() {
        mqttUseCase.apply {
            subscribe(getFullTopic(Mqtt.Topic.TTS), 0)
            subscribe(getFullTopic(Mqtt.Topic.STT), 0)
            subscribe(getFullTopic(Mqtt.Topic.IMAGE), 0)
            subscribe(getFullTopic(Mqtt.Topic.TABLET), 0)
            subscribe(getFullTopic(Mqtt.Topic.ARGV), 0)
        }
    }

    private fun getFullTopic(topic: String): String {
        return topic.replace(Regex.escape("{{deviceId}}").toRegex(), _state.value.deviceId)
    }

    private fun onMqttMessageArrived(topic: String, message: String) {
        when (topic) {
            getFullTopic(Mqtt.Topic.TTS) -> {
                _state.value = _state.value.copy(caption = message)
            }
            getFullTopic(Mqtt.Topic.STT) -> {
                val caption = message.replaceFirstChar { it.uppercase() }
                    .split(": ")
                    .joinToString(": ") { sentence -> sentence.replaceFirstChar { it.uppercase() } }
                _state.value = _state.value.copy(responseCaption = caption)
            }
        }
    }

}