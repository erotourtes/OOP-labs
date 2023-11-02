package com.github.erotourtes.drawing.history

class History {
    private val undoStack = mutableListOf<Command>()
    private val redoStack = mutableListOf<Command>()

    fun undo() {
        undoStack.removeLastOrNull()?.let {
            it.undo()
            redoStack.add(it)
        }
    }

    fun redo() {
        redoStack.removeLastOrNull()?.let {
            it.execute()
            undoStack.add(it)
        }
    }

    fun add(command: Command) {
        undoStack.add(command)
        redoStack.clear()
    }
}