package com.github.erotourtes.utils

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

const val PROCESS_UPDATE_TIME = 100L

const val DESTROY = "__destroy__"
const val EMPTY = "__EMPTY__"
const val DATA = "__DATA__"