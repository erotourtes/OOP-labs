package com.github.erotourtes.utils

class ProcessSender(val process: Process) {
    private val outputStream = process.outputStream.bufferedWriter()

    fun writeMessage(message: String) {
        outputStream.write(message)
        outputStream.newLine()
        outputStream.flush()
    }

    fun close() {
        outputStream.close()
    }

    val isAlive: Boolean
        get() = process.isAlive
}