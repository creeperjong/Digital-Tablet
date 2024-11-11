package com.example.digitaltablet.presentation.robot

import androidx.lifecycle.ViewModel
import com.example.digitaltablet.domain.usecase.MqttUseCase
import com.example.digitaltablet.domain.usecase.RcslUseCase
import com.example.digitaltablet.util.Constants.Mqtt
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

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
                setConnectInfos(event.deviceId)
            }
            is TabletEvent.ConnectMqttBroker -> {
                connectMqtt()
            }
            is TabletEvent.DisconnectMqttBroker -> {
                disconnectMqtt()
            }
        }
    }

    private fun setConnectInfos(deviceId: String) {
        _state.value = _state.value.copy(
            deviceId = deviceId
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