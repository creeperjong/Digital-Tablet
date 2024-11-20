package com.example.digitaltablet.presentation.tablet

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import com.example.digitaltablet.domain.usecase.MqttUseCase
import com.example.digitaltablet.domain.usecase.RcslUseCase
import com.example.digitaltablet.util.Constants.Mqtt
import com.google.common.collect.Table
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
        }
    }

    private fun setConnectInfos(deviceId: String, apiKey: String, asstId: String) {
        _state.value = _state.value.copy(
            deviceId = deviceId
        )
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
        val maxPage = _state.value.imageSources.size
        _state.value = _state.value.copy(
            imageIdx = max(min(page, maxPage - 1), 0),
            canvasTapPositions = emptyList()
        )
    }

    /*
     *  R&T related functions
     */


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
                },
                onMessageArrived = { topic, message ->
                    onMqttMessageArrived(topic, message)
                }
            )
        }
    }

    private fun disconnectMqtt() {
        mqttUseCase.disconnect()
        mqttUseCase.unbindService()
    }

    private fun initialSubscription() {
        mqttUseCase.apply {
            // TODO
        }
    }

    private fun getFullTopic(topic: String): String {
        return topic.replace(Regex.escape("{{deviceId}}").toRegex(), _state.value.deviceId)
    }

    private fun onMqttMessageArrived(topic: String, message: String) {
        when (topic) {
            // TODO
        }
    }

}