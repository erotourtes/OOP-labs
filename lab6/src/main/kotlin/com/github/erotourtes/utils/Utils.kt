package com.github.erotourtes.utils

import java.io.File

fun runJarFile(file: File): Process? {
    val java = "${System.getProperty("java.home")}/bin/java"
    val path = file.absolutePath
    return Runtime.getRuntime().exec("$java -jar $path")
}

const val DESTROY = "destroy"