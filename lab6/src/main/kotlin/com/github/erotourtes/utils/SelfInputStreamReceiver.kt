package com.github.erotourtes.utils

import javafx.application.Platform
import javafx.beans.property.SimpleStringProperty
import java.io.*
import java.lang.management.ManagementFactory
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.thread
import kotlin.system.exitProcess

class SelfInputStreamReceiver {
    val inputMessage = SimpleStringProperty()
    private var isReading = AtomicBoolean(true)
    private var readerThread: Thread? = null
    private val reader = BufferedReader(InputStreamReader(System.`in`))

    init {
        val processId = ManagementFactory.getRuntimeMXBean().name
        readerThread = thread(start = true, isDaemon = false) {
            runNotify("$processId read stream SelfInputStreamReceiver")
            while (isReading.get() && !Thread.currentThread().isInterrupted) {
                if (!reader.ready()) {
                    Thread.sleep(100)
                    continue
                }
                val input = reader.readLine()
                Platform.runLater {
                    inputMessage.value = EMPTY
                    inputMessage.value = input
                }
            }
            runNotify("$processId interrupted SelfInputStreamReceiver")
        }
    }

    fun close() {
        isReading.set(false)
        reader.close()
        readerThread?.interrupt()
        readerThread?.join()
        inputMessage.value = null

        exitProcess(0) // TODO: fix the platform is not exit on thread close in the controller.dispose.pReceiver.close() // htink about disposing in a new thread
    }
}