package com.github.erotourtes.utils

import kotlin.concurrent.thread

class ProcessReceiver(process: Process) {
    private val input = process.inputStream.bufferedReader()
    private var thread: Thread? = null

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