package com.github.erotourtes.view

import com.github.erotourtes.drawing.CanvasController
import com.github.erotourtes.drawing.EditorHandler
import com.github.erotourtes.drawing.history.History
import com.github.erotourtes.drawing.editor.*
import com.github.erotourtes.drawing.shape.*
import com.github.erotourtes.utils.EditorFactory
import com.github.erotourtes.utils.EditorInfo
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import javafx.scene.canvas.Canvas
import javafx.scene.layout.Pane
import tornadofx.*

class MainController : Controller() {
    private val cm by inject<CanvasModel>()
    private val eim by inject<EditorsInfoModel>()
    private val canvas = Canvas()
    private val shapes = ShapesList()
    private val history = History()
    private lateinit var editorHandler: EditorHandler

    private val editorsInfo = listOf(
        EditorInfo("Dot", "Dot", { s, g -> PointEditor(s, g, history) }, FontAwesomeIcon.DOT_CIRCLE_ALT),
        EditorInfo("Line", "Line", { s, g -> ShapeEditor(Line(g), s, g, history) }, FontAwesomeIcon.MINUS),
        EditorInfo("Rectangle", "Rectangle", { s, g -> ShapeEditor(Rect(g), s, g, history) }, FontAwesomeIcon.SQUARE),
        EditorInfo("Ellipse", "Ellipse", { s, g -> ShapeEditor(Ellipse(g), s, g, history) }, FontAwesomeIcon.CIRCLE_ALT),
        EditorInfo("Dumbbell", "Dumbbell", { s, g -> ShapeEditor(Dumbbell(g), s, g, history) }, FontAwesomeIcon.MARS),
        EditorInfo("Cube", "Cube", { s, g -> ShapeEditor(Cube(g), s, g, history) }, FontAwesomeIcon.CUBE),
        EditorInfo("CubeEx", "CubeEx", { s, g -> ShapeEditor(CubeEx(g), s, g, history) }, FontAwesomeIcon.CUBES),
    )

    fun populate() {
        val factories = editorsInfo.associate { it.name to it.editorFactory }.toMutableMap().apply {
            this[EmptyEditor::class.java.name] = EditorFactory { s, g -> EmptyEditor(s, g, history) }
        }

        editorHandler = EditorHandler(shapes, factories, canvas)
            .apply { useEditor(EmptyEditor::class.java.name) }
        cm.item = CanvasData(shapes, editorHandler, history, CanvasController(canvas))

        eim.editorsInfo.value = editorsInfo.asObservable()
    }

    fun bindCanvas(pane: Pane) {
        pane += canvas
        canvas.widthProperty().bind(pane.widthProperty())
        canvas.heightProperty().bind(pane.heightProperty())
        canvas.widthProperty().addListener { _,_,_ -> editorHandler.getEditor().redraw() }
        canvas.heightProperty().addListener { _,_,_ -> editorHandler.getEditor().redraw() }
    }

    fun undo() = history.undo()
    fun redo() = history.redo()
}