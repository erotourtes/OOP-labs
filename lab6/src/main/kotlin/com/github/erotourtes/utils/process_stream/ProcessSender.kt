package com.github.erotourtes.utils.process_stream

import com.github.erotourtes.utils.Closable
import com.github.erotourtes.utils.Logger
import com.github.erotourtes.utils.MessageType

class ProcessSender(process: Process, private val formatter: (MessageType, String) -> String) : Closable {
    private val outputStream = process.outputStream.bufferedWriter()

    fun send(type: MessageType, message: String = "") {
        runCatching {
            val combined = formatter(type, message)
            outputStream.write(combined)
            outputStream.newLine()
            outputStream.flush()
            Logger.log("write: $message")
        }.onFailure {
            Logger.log("ProcessSender(writeMessage): ${it.message}", Logger.InfoType.ERROR)
        }
    }

    override fun close() {
        runCatching { outputStream.close() }.onFailure {
            Logger.log("ProcessSender(close): ${it.message}", Logger.InfoType.ERROR)
        }
    }
}