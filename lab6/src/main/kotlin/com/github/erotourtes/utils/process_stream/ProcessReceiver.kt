package com.github.erotourtes.utils.process_stream

import com.github.erotourtes.utils.*
import javafx.beans.property.SimpleStringProperty
import javafx.beans.value.ObservableValue
import kotlin.concurrent.thread

class ProcessReceiver(process: Process) : Closable, StringObservable {
    private val inputStream = process.inputStream.bufferedReader()
    private var thread: Thread? = null
    private val input = SimpleStringProperty()

    init {
        thread = thread {
            while (!Thread.currentThread().isInterrupted) {
                if (!inputStream.ready()) {
                    val res = runCatching { Thread.sleep(PROCESS_UPDATE_TIME) }
                    if (res.isFailure) break
                    continue
                }

                val inputData = inputStream.readLine()
                if (input.value == inputData) input.value = MessageType.EMPTY.type
                input.value = inputData

                Logger.log("INPUT: ${input.value}")
            }
            Logger.log("ProcessReceiver(interrupted-end)", Logger.InfoType.WARNING)
        }
    }

    override fun close() {
        inputStream.close()
        thread?.interrupt()
        thread?.join()
    }

    override fun getObservable(): ObservableValue<String> = input
}