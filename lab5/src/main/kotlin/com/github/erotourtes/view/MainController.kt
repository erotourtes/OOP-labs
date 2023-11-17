package com.github.erotourtes.view

import com.github.erotourtes.drawing.CanvasController
import com.github.erotourtes.drawing.EditorHandler
import com.github.erotourtes.drawing.history.History
import com.github.erotourtes.drawing.editor.*
import com.github.erotourtes.drawing.shape.*
import com.github.erotourtes.utils.EditorInfo
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import javafx.scene.canvas.Canvas
import javafx.scene.layout.Pane
import tornadofx.*

class MainController : Controller() {
    private val cm by inject<CanvasModel>()
    private val eim by inject<EditorsInfoModel>()
    private val canvas = Canvas()
    private val shapeList = ShapesList()
    private val history = History()
    private lateinit var editorHandler: EditorHandler

    private val editorsInfo = listOf(
        EditorInfo("Dot", "Dot", Point::class.java to PointEditor.getInstance(), FontAwesomeIcon.DOT_CIRCLE_ALT),
        EditorInfo("Line", "Line", Line::class.java to ShapeEditor, FontAwesomeIcon.MINUS),
        EditorInfo("Rectangle", "Rectangle", Rect::class.java to ShapeEditor, FontAwesomeIcon.SQUARE),
        EditorInfo("Ellipse", "Ellipse", Ellipse::class.java to ShapeEditor, FontAwesomeIcon.CIRCLE_ALT),
        EditorInfo("Dumbbell", "Dumbbell", Dumbbell::class.java to ShapeEditor, FontAwesomeIcon.MARS),
        EditorInfo("Cube", "Cube", Cube::class.java to ShapeEditor, FontAwesomeIcon.CUBE),
        EditorInfo("CubeEx", "CubeEx", CubeEx::class.java to ShapeEditor, FontAwesomeIcon.CUBES),
    )

    private fun populateEditors() {
        val gc = canvas.graphicsContext2D
        editorsInfo.forEach {
            val (_, editor) = it.pair
            editor.init(shapeList, gc, history)
        }
        EmptyEditor.init(shapeList, gc, history)
    }

    fun populate() {
        populateEditors()

        val maps = editorsInfo.associate { it.pair } + (EmptyShape.javaClass to EmptyEditor)
        editorHandler = EditorHandler(maps)

        cm.item = CanvasData(shapeList, editorHandler, history, CanvasController(canvas))
        eim.editorsInfo.value = editorsInfo.asObservable()
    }

    fun bindCanvas(pane: Pane) {
        pane += canvas
        canvas.widthProperty().bind(pane.widthProperty())
        canvas.heightProperty().bind(pane.heightProperty())
        canvas.widthProperty().addListener { _, _, _ -> editorHandler.editor.redraw() }
        canvas.heightProperty().addListener { _, _, _ -> editorHandler.editor.redraw() }
    }

    fun undo() = history.undo()
    fun redo() = history.redo()
}