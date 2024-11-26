package com.example.digitaltablet.domain.usecase

import android.util.Log
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
        repository.connect(host, deviceId + "_tablet", onConnected, onMessageArrived)
    }

    fun disconnect(onDisconnected: () -> Unit) {
        repository.disconnect(onDisconnected)
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