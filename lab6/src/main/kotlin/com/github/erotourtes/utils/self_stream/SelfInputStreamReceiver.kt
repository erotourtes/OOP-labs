package com.github.erotourtes.utils.self_stream

import com.github.erotourtes.utils.*
import javafx.application.Platform
import javafx.beans.property.SimpleStringProperty
import java.io.*
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.thread

class SelfInputStreamReceiver : Closable, StringObservable {
    private val input = SimpleStringProperty()
    private var isReading = AtomicBoolean(true)
    private var readerThread: Thread? = null
    private val inputStream = BufferedReader(InputStreamReader(System.`in`))

    init {
        readerThread = thread(start = true, isDaemon = false) {
            while (isReading.get() && !Thread.currentThread().isInterrupted) {
                if (!inputStream.ready()) {
                    val res = runCatching { Thread.sleep(PROCESS_UPDATE_TIME) }
                    if (res.isFailure) break
                    continue
                }
                val input = inputStream.readLine()
                Platform.runLater {
                    if (input == this.input.value) this.input.value = MessageType.EMPTY.type
                    this.input.value = input
                    Logger.log("INPUT: $input")
                }
            }
            Logger.log("SelfInputStreamReceiver(interrupt-end)", Logger.InfoType.WARNING)
        }
    }

    override fun close() {
        isReading.set(false)
        inputStream.close()
        readerThread?.interrupt() // TODO: fix it is not interrupting
        readerThread?.join()
        input.value = MessageType.EMPTY.type
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

    override fun getObservable() = input
}