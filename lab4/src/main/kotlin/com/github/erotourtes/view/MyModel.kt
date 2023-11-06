package com.github.erotourtes.view

import com.github.erotourtes.drawing.CanvasController
import com.github.erotourtes.drawing.EditorHandler
import com.github.erotourtes.drawing.history.History
import com.github.erotourtes.drawing.editor.ShapesList
import com.github.erotourtes.utils.EditorInfo
import javafx.beans.property.SimpleListProperty
import tornadofx.*

data class CanvasData(
    val shapesList: ShapesList,
    val editorHandler: EditorHandler,
    val history: History,
    val canvasController: CanvasController,
)

class CanvasModel : ItemViewModel<CanvasData>() {
    private val shapesList = bind(CanvasData::shapesList)
    private val editorHandler = bind(CanvasData::editorHandler)
    private val history = bind(CanvasData::history)
    private val canvasController = bind(CanvasData::canvasController)

    val eh: EditorHandler by editorHandler
    val sl: ShapesList by shapesList
    val h: History by history
    val cc: CanvasController by canvasController
}

class EditorsInfoData {
    val editorsInfo = SimpleListProperty<EditorInfo>()
}

class EditorsInfoModel : ItemViewModel<EditorsInfoData>() {
    val editorsInfo = bind(EditorsInfoData::editorsInfo)
}