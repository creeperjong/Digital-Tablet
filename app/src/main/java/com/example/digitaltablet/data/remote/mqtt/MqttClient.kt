package com.example.digitaltablet.data.remote.mqtt

import android.content.Context
import android.util.Log
import com.example.digitaltablet.util.Constants.Mqtt
import info.mqtt.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage
import kotlin.jvm.Throws

class MqttClient {
    private val logTag = "MqttClient"

    fun getMqttClient(context: Context, serverUri: String, clientId: String): MqttAndroidClient {
        val mqttAndroidClient = MqttAndroidClient(context, serverUri, clientId)
        try {
            val token = mqttAndroidClient.connect(getMqttConnectionOption())
            token.actionCallback = object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    mqttAndroidClient.setBufferOpts(getDisconnectedBufferOptions())
                    Log.d(logTag, "Success")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.d(logTag, "Failure $exception")
                }

            }
        } catch (e: MqttException) {
            e.printStackTrace()
        }
        return mqttAndroidClient
    }

    @Throws(MqttException::class)
    fun subscribe(client: MqttAndroidClient, topic: String, qos: Int) {
        val token = client.subscribe(topic, qos)
        token.actionCallback = object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                Log.d(logTag, "Subscribe Successfully")
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                Log.d(logTag, "Subscribe Failed $topic")
            }
        }
    }

    @Throws(MqttException::class)
    fun publish(client: MqttAndroidClient, topic: String, message: String, qos: Int) {
        val mqttMessage = MqttMessage(message.toByteArray())
        mqttMessage.qos = qos
        val token = client.publish(topic, mqttMessage)
        token.actionCallback = object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                Log.d(logTag, "Publish Successfully")
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                Log.d(logTag, "Publish Failed $topic")
            }

        }
    }

    private fun getMqttConnectionOption(): MqttConnectOptions {
        val mqttConnectOptions = MqttConnectOptions()

        mqttConnectOptions.connectionTimeout = 30
        mqttConnectOptions.keepAliveInterval = 180
        mqttConnectOptions.isAutomaticReconnect = true
        mqttConnectOptions.isCleanSession = true

        // Set username and password
        mqttConnectOptions.userName = Mqtt.USERNAME
        mqttConnectOptions.password = Mqtt.PASSWORD.toCharArray()

        return mqttConnectOptions
    }

    private fun getDisconnectedBufferOptions(): DisconnectedBufferOptions {
        val disconnectedBufferOptions = DisconnectedBufferOptions()

        disconnectedBufferOptions.isBufferEnabled = true
        disconnectedBufferOptions.bufferSize = 100
        disconnectedBufferOptions.isPersistBuffer = false
        disconnectedBufferOptions.isDeleteOldestMessages = false

        return disconnectedBufferOptions
    }

}