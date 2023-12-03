package com.github.erotourtes.process_communicators

import javafx.application.Platform
import javafx.beans.property.SimpleStringProperty
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.concurrent.thread

class ProcessSelfReceiver {
    val inputMessage = SimpleStringProperty()

    private val reader = BufferedReader(InputStreamReader(System.`in`))
    private var isReading = true

    fun run() {
        thread {
            readStream()
        }
    }

    private fun readStream() {
        while (isReading) {
            val input = reader.readLine()
            Platform.runLater {
                inputMessage.value = input
            }
        }
    }

    fun close() {
        isReading = false
        reader.close()
    }
}