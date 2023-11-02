package com.github.erotourtes.view

import com.github.erotourtes.drawing.EditorHandler
import com.github.erotourtes.drawing.history.History
import com.github.erotourtes.drawing.editor.ShapesList
import com.github.erotourtes.utils.EditorInfo
import javafx.collections.ObservableList
import javafx.scene.canvas.Canvas
import tornadofx.*

data class CanvasData(
    val canvas: Canvas,
    val shapesList: ShapesList,
    val editorsInfo: ObservableList<EditorInfo>,
    val editorHandler: EditorHandler,
    val history: History
)

class CanvasModel : ItemViewModel<CanvasData>() {
    private val canvas = bind(CanvasData::canvas)
    private val shapesList = bind(CanvasData::shapesList)
    private val editorsInfo = bind(CanvasData::editorsInfo)
    private val editorHandler = bind(CanvasData::editorHandler)
    private val history = bind(CanvasData::history)

    val eh: EditorHandler
        get() = editorHandler.value

    val c: Canvas
        get() = canvas.value

    val sl: ShapesList
        get() = shapesList.value

    val ei: ObservableList<EditorInfo>
        get() = editorsInfo.value

    val h: History
        get() = history.value
}

class MyScope : Scope() {
    val canvasModel = CanvasModel()
}