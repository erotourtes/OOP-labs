package com.github.erotourtes.utils

import java.io.File

fun runJarFile(file: File): Process? {
    val java = "${System.getProperty("java.home")}/bin/java"
    val path = file.absolutePath
    return Runtime.getRuntime().exec("$java -jar $path")
}

fun logger(message: String) {
//    val tempPath = "/home/sirmax/Files/Documents/projects/kotlin/oop-labs-creating-last-step/lab6/out/artifacts/log.txt"
//    File(tempPath).appendText("$message\n")
    println(message)
}

const val DESTROY = "__destroy__"
const val EMPTY = "__EMPTY__"