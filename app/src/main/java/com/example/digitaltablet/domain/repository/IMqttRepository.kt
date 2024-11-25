package com.example.digitaltablet.domain.repository

interface IMqttRepository {
    fun connect(
        host: String,
        deviceId: String,
        onConnected: () -> Unit,
        onMessageArrived: (String, String) -> Unit
    )
    fun disconnect(onDisconnected: () -> Unit)
    fun subscribe(topic: String, qos: Int)
    fun publish(topic: String, message: String, qos: Int)
    fun bindService(onServiceConnected: () -> Unit)
    fun unbindService()
}