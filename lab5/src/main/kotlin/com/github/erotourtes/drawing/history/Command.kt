package com.github.erotourtes.drawing.history

interface Command {
    fun execute()
    fun undo()
}