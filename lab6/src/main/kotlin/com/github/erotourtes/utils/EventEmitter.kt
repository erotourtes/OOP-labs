package com.github.erotourtes.utils

class EventEmitter<T>(private val source: T) where T : Closable, T : StringObservable {
    private val listeners = mutableMapOf<MessageType, MutableList<(String) -> Unit>>()

    init {
        val observable = source.getObservable()
        observable.addListener { _, _, newMsg ->
            if (newMsg.isEmpty()) return@addListener

            val type = MessageType.getTypeOf(newMsg)
            listeners[type]?.forEach { it(newMsg.removePrefix(type.type).trim()) }
        }
    }

    fun subscribe(event: MessageType, listener: (String) -> Unit) {
        listeners.getOrPut(event) { mutableListOf() }.add(listener)
    }

    fun close() {
        listeners.clear()
        source.close()
    }

    companion object {
        fun format(event: MessageType, message: String = "") = "${event.type} $message"

        fun getFormatter() = { event: MessageType, message: String? -> format(event, message ?: "") }
    }
}