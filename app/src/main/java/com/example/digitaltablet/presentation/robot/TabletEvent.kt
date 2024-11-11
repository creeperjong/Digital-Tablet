package com.example.digitaltablet.presentation.robot

sealed class TabletEvent {

    data object ClearToastMsg: TabletEvent()

    data class SetConnectInfos(
        val deviceId: String,
        val apiKey: String,
        val asstId: String,
    ): TabletEvent()

    data object ConnectMqttBroker: TabletEvent()

    data object DisconnectMqttBroker: TabletEvent()

}