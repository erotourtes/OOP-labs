package com.github.erotourtes.utils

import javafx.beans.property.StringProperty
import javafx.beans.value.ObservableValue
import java.io.File

fun runJarFile(file: File): Process? {
    val java = "${System.getProperty("java.home")}/bin/java"
    val path = file.absolutePath
    return Runtime.getRuntime().exec("$java -jar $path")
}

object Logger {
    var isLogging = true
    var preMessage = ""

    enum class InfoType {
        INFO, ERROR, WARNING
    }

    val color = mapOf(
        InfoType.INFO to "\u001B[32m",
        InfoType.ERROR to "\u001B[31m",
        InfoType.WARNING to "\u001B[33m",
    )

    val reset = "\u001B[0m"

    fun log(message: String, type: InfoType = InfoType.INFO) {
        if (!isLogging) return

        println("${color[type]}$preMessage: $message$reset")
    }
}

object ListConverter {
    inline fun <reified T> toString(list: List<T>): String = list.joinToString(",")

    inline fun <reified T> toList(string: String, converter: (String) -> T): List<T> {
        if (string.isEmpty()) return emptyList()
        return string.split(",").map {
            converter(it.trim())
        }
    }
}

const val PROCESS_UPDATE_TIME = 100L

enum class MessageType(val type: String) {
    EMPTY("__EMPTY__"),
    DATA("__DATA__"),
    DESTROY("__DESTROY__"),
    ON_DESTROY("__ON_DESTROY__");


    companion object {
        fun getTypeOf(message: String): MessageType =
            MessageType.entries.find { isOfType(it, message) } ?: EMPTY

        fun isOfType(type: MessageType, message: String) = message.startsWith(type.type)
    }
}

interface Closable {
    fun close()
}

interface StringObservable {
    fun getObservable(): ObservableValue<String>
}