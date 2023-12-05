package com.github.erotourtes.utils

import javafx.beans.property.SimpleStringProperty
import kotlin.concurrent.thread

class ProcessReceiver(process: Process) {
    private val input = process.inputStream.bufferedReader()
    private var thread: Thread? = null
    val inputData = SimpleStringProperty()

    init {
        thread = thread {
            while (!Thread.currentThread().isInterrupted) {
                if (!input.ready()) {
                    val res = runCatching { Thread.sleep(PROCESS_UPDATE_TIME) }
                    if (res.isFailure) break
                    continue
                }
                val input = input.readLine()
                Logger.log("INPUT: $input")

                if (input.startsWith(DATA)) {
                    val dataStart = input.indexOf('{')
                    val dataEnd = input.length - 1
                    inputData.value = input.substring(dataStart + 1, dataEnd)
                }
            }
            Logger.log("ProcessReceiver(interrupted-end)", Logger.InfoType.WARNING)
        }
    }

    fun close() {
        input.close()
        thread?.interrupt()
        thread?.join()
    }
}