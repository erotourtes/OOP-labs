package com.github.erotourtes.utils

import javafx.application.Platform
import javafx.beans.property.SimpleStringProperty
import java.io.*
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.thread

class SelfInputStreamReceiver {
    val inputMessage = SimpleStringProperty()
    private var isReading = AtomicBoolean(true)
    private var readerThread: Thread? = null
    private val reader = BufferedReader(InputStreamReader(System.`in`))

    init {
        readerThread = thread(start = true, isDaemon = false) {
            while (isReading.get() && !Thread.currentThread().isInterrupted) {
                if (!reader.ready()) {
                    val res = runCatching { Thread.sleep(PROCESS_UPDATE_TIME) }
                    if (res.isFailure) break
                    continue
                }
                val input = reader.readLine()
                Platform.runLater {
                    if (input == inputMessage.value) inputMessage.value = EMPTY
                    inputMessage.value = input
                    Logger.log("INPUT: $input")
                }
            }
            Logger.log("SelfInputStreamReceiver(interrupt-end)", Logger.InfoType.WARNING)
        }
    }

    fun close() {
        isReading.set(false)
        reader.close()
        readerThread?.interrupt() // TODO: fix it is not interrupting
        readerThread?.join()
        inputMessage.value = EMPTY
        /*
            used to receive `null` (now EMPTY) msg in the inputMessage listener
            because FirstController.dispose when close stream sets inputMessage to null
            and then from the JavaFX thread I call Platform.exit()
            if I don't call Platform.exit() then the app will not close and the process will not be destroyed,
            even if stop() method is called

            So the sequence is:
            1. App.close() -> 2. FirstController.dispose() -> 3. SelfInputStreamReceiver.close() -> 4. Platform.exit()

            All because the documentation says:
            "The implementation of this (app.stop()) method provided by the Application class does nothing.",
            which is lie because it stops the app
            (maybe it stops the javafx thread, but I don't have my own daemon threads, so the process stops)
         */
    }
}

class SelfOutputStreamSender {
    private fun formatMessage(clazz: Class<*>, message: String) = "$DATA(${clazz.name}): {$message}"

    fun send(clazz: Class<*>, message: String) {
        Logger.log("OUTPUT: $message")
        println(formatMessage(clazz, message))
    }

    fun send(msg: String) {
        Logger.log("OUTPUT: $msg")
        println(msg)
    }
}