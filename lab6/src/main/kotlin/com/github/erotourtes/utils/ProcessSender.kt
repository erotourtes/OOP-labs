package com.github.erotourtes.utils

class ProcessSender(process: Process) {
    private val outputStream = process.outputStream.bufferedWriter()

    fun writeMessage(message: String) {
        runCatching {
            outputStream.write(message)
            outputStream.newLine()
            outputStream.flush()
            Logger.log("write: $message")
        }.onFailure {
            Logger.log("ProcessSender(writeMessage): ${it.message}", Logger.InfoType.ERROR)
        }
    }

    fun close() {
        runCatching { outputStream.close() }.onFailure {
            Logger.log("ProcessSender(close): ${it.message}", Logger.InfoType.ERROR)
        }
    }
}