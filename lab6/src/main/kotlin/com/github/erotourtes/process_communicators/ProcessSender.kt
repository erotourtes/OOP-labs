package com.github.erotourtes.process_communicators

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
}