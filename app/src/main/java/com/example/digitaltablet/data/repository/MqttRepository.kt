package com.example.digitaltablet.data.repository

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import com.example.digitaltablet.data.remote.mqtt.MqttMessageService
import com.example.digitaltablet.domain.repository.IMqttRepository
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MqttRepository(
    private val context: Context
) : IMqttRepository {
    private var mqttService: MqttMessageService? = null
    private var isBound = false
    private var isConnected = false
    private var onServiceConnectedCallback: (() -> Unit)? = null

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MqttMessageService.LocalBinder
            mqttService = binder.service
            isBound = true
            onServiceConnectedCallback?.invoke()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            mqttService = null
            isBound = false
        }

    }

    override fun connect(
        host: String,
        deviceId: String,
        onConnected: () -> Unit,
        onMessageArrived: (String, String) -> Unit
    ) {
        if (isBound and !isConnected) {
            mqttService?.connect(host, deviceId, {
                isConnected = true
                onConnected()
            }, onMessageArrived)
        }
    }

    override fun subscribe(topic: String, qos: Int) {
        if (isBound and isConnected) {
            mqttService?.subscribe(topic, qos)
        }
    }

    override fun publish(topic: String, message: String, qos: Int) {
        if (isBound and isConnected) {
            mqttService?.publish(topic, message, qos)
        }
    }

    override fun disconnect(onDisconnected: () -> Unit) {
        if (isBound and isConnected) {
            mqttService?.disconnect {
                isConnected = false
                onDisconnected()
            }
        }
    }

    override fun bindService(onServiceConnected: () -> Unit) {
        onServiceConnectedCallback = onServiceConnected
        if (!isBound) {
            val intent = Intent(context, MqttMessageService::class.java)
            context.startService(intent)
            context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
            isBound = true
        } else {
            onServiceConnectedCallback!!.invoke()
        }
    }

    override fun unbindService() {
        if (isBound) {
            context.unbindService(serviceConnection)
            isBound = false
        }
    }

}