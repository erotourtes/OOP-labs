package com.github.erotourtes.utils

import javafx.application.Platform
import javafx.beans.property.SimpleStringProperty
import java.io.*
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.thread
import kotlin.reflect.KFunction
import kotlin.reflect.KProperty0

class StreamUtils {
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
        inputMessage.value = null

//        exitProcess(0) // TODO: fix the platform is not exit on thread close in the controller.dispose.pReceiver.close() // htink about disposing in a new thread
    }
}

class SelfOutputStreamSender {
    private fun formatMessage(clazz: Class<*>, message: String) = "$DATA(${clazz.name}): {$message}"
    fun send(property: KProperty0<String>) {
        val message = property.get()
        Logger.log("OUTPUT: $message")
        println(formatMessage(property.javaClass, message))
    }

    fun send(clazz: Class<*>, message: String) {
        Logger.log("OUTPUT: $message")
        println(formatMessage(clazz, message))
    }
}