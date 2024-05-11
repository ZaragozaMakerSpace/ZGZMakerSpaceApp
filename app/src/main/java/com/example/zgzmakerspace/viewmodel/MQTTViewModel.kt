package com.example.zgzmakerspace.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence

class MqttViewModel : ViewModel() {
    val makerspaceIsOpen = mutableStateOf(false)
    val connectionStatus = mutableStateOf("Desconectado")
    private val serverURI = "tcp://broker.hivemq.com:1883"
    private val clientId = "KotlinMQTTClient"
    private val topic = "ZMSDoor"
    private lateinit var mqttClient: MqttClient
    private val TAG = "MQTT"

    init {
        setupMqttClient()
    }

    private fun setupMqttClient() {
        try {
            val persistence = MemoryPersistence()
            val mqttClient = MqttClient(serverURI, clientId, persistence)
            val options = MqttConnectOptions()
            options.isCleanSession = true
            options.setConnectionTimeout(10)

            mqttClient.connect(options)

            mqttClient.setCallback(object : MqttCallbackExtended {
                override fun connectComplete(reconnect: Boolean, serverURI: String) {
                    connectionStatus.value = "Connected"
                    Log.d(TAG, "Connection Succesful")
                    mqttClient.subscribe(
                        topic,
                        1
                    )
                    Log.d(TAG, "Connected to: $serverURI")
                }

                override fun messageArrived(topic: String?, message: MqttMessage?) {
                    message?.let {
                        val msg = String(it.payload)
                        makerspaceIsOpen.value = msg == "open"
                        Log.d("MQTT message", message.toString())
                        Log.d("MQTT payload", msg)
                    }
                }

                override fun connectionLost(cause: Throwable?) {
                    Log.d(TAG, "Connection lost: ${cause?.message}")
                    connectionStatus.value = "NotConnected"
                }

                override fun deliveryComplete(token: IMqttDeliveryToken?) {
                    // Not used in this scenario
                    Log.d(TAG, "Delivery Complete")
                }
            })

            mqttClient.subscribe(topic, 1)
        } catch (e: MqttException) {
            Log.d(TAG, e.message.toString())
            e.printStackTrace()
        }
    }
}