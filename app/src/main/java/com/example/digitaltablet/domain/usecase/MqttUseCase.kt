package com.example.digitaltablet.domain.usecase

import com.example.digitaltablet.data.repository.MqttRepository
import com.example.digitaltablet.domain.repository.IMqttRepository
import javax.inject.Inject

class MqttUseCase (
    private val repository: IMqttRepository
) {
    fun connect(
        host: String,
        deviceId: String,
        onConnected: () -> Unit,
        onMessageArrived: (String, String) -> Unit
    ) {
        repository.connect(host, deviceId, onConnected, onMessageArrived)
    }

    fun disconnect() {
        repository.disconnect()
    }

    fun bindService(onServiceConnected: () -> Unit) {
        repository.bindService(onServiceConnected)
    }

    fun unbindService() {
        repository.unbindService()
    }

    fun publish(topic: String, message: String, qos: Int) {
        repository.publish(topic, message, qos)
    }

    fun subscribe(topic: String, qos: Int) {
        repository.subscribe(topic, qos)
    }
}