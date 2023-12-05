package com.github.erotourtes.utils.self_stream

import com.github.erotourtes.utils.Logger
import com.github.erotourtes.utils.MessageType

class SelfOutputStreamSender(private val formatter: (MessageType, String) -> String) {
    fun send(message: String) {
        Logger.log("OUTPUT: $message")
        println(formatter(MessageType.DATA, message))
    }

    fun send(type: MessageType) {
        Logger.log("OUTPUT: $type")
        println(formatter(type, ""))
    }
}