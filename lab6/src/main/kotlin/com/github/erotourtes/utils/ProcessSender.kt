package com.github.erotourtes.utils

class ProcessSender(val process: Process) {
    private val outputStream = process.outputStream.bufferedWriter()

    fun writeMessage(message: String) {
        try {
            outputStream.write(message)
            outputStream.newLine()
            outputStream.flush()
            Logger.log("write: $message")
        } catch (e: Exception) {
            Logger.log("ProcessSender(writeMessage): $e", Logger.InfoType.ERROR)
        }
    }

    fun close() {
        try {
            outputStream.close()
        } catch (e: Exception) {
            Logger.log("ProcessSender(close): $e", Logger.InfoType.ERROR)
        }
    }

    val isAlive: Boolean
        get() = process.isAlive
}