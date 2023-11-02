package com.github.erotourtes.view

import com.github.erotourtes.drawing.EditorHandler
import com.github.erotourtes.drawing.editor.*
import com.github.erotourtes.drawing.shape.*
import com.github.erotourtes.utils.EditorFactory
import com.github.erotourtes.utils.EditorInfo
import com.github.erotourtes.utils.n
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import javafx.collections.FXCollections
import javafx.scene.canvas.Canvas
import javafx.scene.layout.Pane
import tornadofx.*

class MainController : Controller() {
    private val cm: CanvasModel by inject()
    private val canvas = Canvas()
    private val shapes = ShapesList(n)

    private val editorsInfo = FXCollections.observableArrayList(
        EditorInfo("Dot", "Dot", { s, g -> PointEditor(s, g) }, FontAwesomeIcon.DOT_CIRCLE_ALT),
        EditorInfo("Line", "Line", { s, g -> ShapeEditor(Line(g), s, g) }, FontAwesomeIcon.MINUS),
        EditorInfo("Rectangle", "Rectangle", { s, g -> ShapeEditor(Rect(g), s, g) }, FontAwesomeIcon.SQUARE),
        EditorInfo("Ellipse", "Ellipse", { s, g -> ShapeEditor(Ellipse(g), s, g) }, FontAwesomeIcon.CIRCLE_ALT),
        EditorInfo("Dumbbell", "Dumbbell", { s, g -> ShapeEditor(Dumbbell(g), s, g) }, FontAwesomeIcon.MARS),
        EditorInfo("Cube", "Cube", { s, g -> ShapeEditor(Cube(g), s, g) }, FontAwesomeIcon.CUBE),
        EditorInfo("CubeEx", "CubeEx", { s, g -> ShapeEditor(CubeEx(g), s, g) }, FontAwesomeIcon.CUBES),
    )

    fun populate() {
        val factories = editorsInfo.associate { it.name to it.editorFactory }.toMutableMap().apply {
            this[EmptyEditor::class.java.name] = EditorFactory { s, g -> EmptyEditor(s, g) }
        }

        val editorHandler = EditorHandler(shapes, factories, canvas)
            .apply { useEditor(EmptyEditor::class.java.name) }
        cm.item = CanvasData(canvas, shapes, editorsInfo, editorHandler)
    }

    fun bindCanvas(pane: Pane) {
        pane += canvas
        canvas.widthProperty().bind(pane.widthProperty())
        canvas.heightProperty().bind(pane.heightProperty())
    }
}