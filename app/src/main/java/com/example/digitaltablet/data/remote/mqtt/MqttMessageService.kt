package com.example.digitaltablet.data.remote.mqtt

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import info.mqtt.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.json.JSONException
import kotlin.jvm.Throws
class MqttMessageService : Service() {

    private var mqttClient: MqttClient? = null
    private var mqttAndroidClient: MqttAndroidClient? = null

    private val logTag = "MqttMessageService"

    fun connect(
        host: String,
        deviceId: String,
        onConnected: () -> Unit,
        onMessageArrived: (String, String) -> Unit
    ) {
        mqttClient = MqttClient()
        mqttAndroidClient = mqttClient!!.getMqttClient(
            applicationContext,
            host,
            deviceId
        )

        mqttAndroidClient!!.setCallback(object : MqttCallbackExtended {
            override fun connectComplete(reconnect: Boolean, serverURI: String?) {
                Log.d(logTag, "Connect Completed $serverURI")
                try {
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(
                            this@MqttMessageService,
                            "MQTT Broker Connected!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    onConnected()
                } catch (e: MqttException) {
                    e.printStackTrace()
                }
            }

            override fun connectionLost(cause: Throwable?) {
                Log.d(logTag, "Connection Lost")

                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(
                        this@MqttMessageService,
                        "MQTT connectionLost",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                connect(host, deviceId, onConnected, onMessageArrived)
            }

            @Throws(Exception::class)
            override fun messageArrived(topic: String?, message: MqttMessage?) {
                Log.d(
                    logTag,
                    """
                        isRetained = ${message?.isRetained} messageArrive $topic, 
                        ${String(message?.payload ?: ByteArray(0))}
                    """.trimIndent()
                )

                try {
                    val msg = String(message?.payload ?: ByteArray(0))
                    onMessageArrived(topic ?: "", msg)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

                if (message?.isRetained == true) {
                    Log.i(logTag, "isRetained")
                    message.clearPayload()
                }
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {
                Log.d(logTag, "Delivery Completed")
            }

        })
    }

    fun disconnect() {
        mqttClient = null
        mqttAndroidClient = null
    }

    fun subscribe(topic: String, qos: Int) {
        mqttClient?.subscribe(mqttAndroidClient!!, topic, qos)
    }

    fun publish(topic: String, message: String, qos: Int) {
        mqttClient?.publish(mqttAndroidClient!!, topic, message, qos)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(logTag, "onStartCommand")
        return Service.START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(logTag, "onDestroy")
    }

    private val mBinder = LocalBinder()

    inner class LocalBinder: Binder() {
        internal val service: MqttMessageService
            get() = this@MqttMessageService
    }

    override fun onBind(p0: Intent?): IBinder {
        return mBinder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        disconnect()
        return super.onUnbind(intent)
    }

}