package com.github.erotourtes.utils

import kotlin.concurrent.thread

class ProcessReceiver(private val process: Process) {
    private val input = process.inputStream.bufferedReader()
    private var thread: Thread? = null

    init {
        thread = thread {
            logger("-------------------------------start ProcessReceiver----------------------------")
            while (!Thread.currentThread().isInterrupted) {
                logger("-------------------------------check ProcessReceiver----------------------------")
                if (!input.ready()) {
                    try {
                        Thread.sleep(100)
                    } catch (e: InterruptedException) {
                        logger("ProcessReceiver(interrupted)")
                        break
                    }
                    continue
                }
                val input = input.readLine()
                logger("ProcessReceiver(read): $input")
            }
            logger("ProcessReceiver(interrupted-end)")
        }
    }

    fun close() {
        input.close()
        thread?.interrupt() // TODO: fix it is not interrupting
        thread?.join()
        logger("ProcessReceiver(close)")
    }
}