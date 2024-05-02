package com.example.zgzmakerspace.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage

class MqttViewModel : ViewModel() {
    val makerspaceIsOpen = mutableStateOf(false)

    private val serverURI = "ssl://broker.hivemq.com:1883"
    private val clientId = MqttClient.generateClientId()
    private val topic = "ZMSDoor"
    private lateinit var mqttClient: MqttClient

    init {
        setupMqttClient()
    }

    private fun setupMqttClient() {
        try {
            mqttClient = MqttClient(serverURI, clientId)

            val mqttClient = MqttClient(serverURI, clientId)
            val options = MqttConnectOptions()
            options.setCleanSession(true)
            options.setConnectionTimeout(10)

            mqttClient.connect()
            /* TODO : Callback with Log if connection is succesfull*/

            mqttClient.setCallback(object : MqttCallback {
                override fun messageArrived(topic: String?, message: MqttMessage?) {
                    message?.let {
                        val msg = String(it.payload)
                        makerspaceIsOpen.value = msg == "open"
                    }
                }

                override fun connectionLost(cause: Throwable?) {
                    // Log.d("MQTT", "Connection lost: ${cause?.message}")
                }

                override fun deliveryComplete(token: IMqttDeliveryToken?) {
                    // Not used in this scenario
                }
            })

            mqttClient.subscribe(topic, 1)
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }
}