package com.github.erotourtes.view

import com.github.erotourtes.drawing.EditorHandler
import com.github.erotourtes.drawing.editor.*
import com.github.erotourtes.drawing.shape.*
import com.github.erotourtes.utils.EditorFactory
import com.github.erotourtes.utils.EditorInfo
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import javafx.scene.canvas.Canvas
import tornadofx.*

class MainController : Controller() {
    val editorsInfo = listOf(
        EditorInfo("Dot", "Dot", { s, g -> PointEditor(s, g) }, FontAwesomeIcon.DOT_CIRCLE_ALT),
        EditorInfo("Line", "Line", { s, g -> ShapeEditor(Line(g), s, g) }, FontAwesomeIcon.MINUS),
        EditorInfo("Rectangle", "Rectangle", { s, g -> ShapeEditor(Rect(g), s, g) }, FontAwesomeIcon.SQUARE),
        EditorInfo("Ellipse", "Ellipse", { s, g -> ShapeEditor(Ellipse(g), s, g) }, FontAwesomeIcon.CIRCLE_ALT),
        EditorInfo("Dumbbell", "Dumbbell", { s, g -> ShapeEditor(Dumbbell(g), s, g) }, FontAwesomeIcon.MARS),
        EditorInfo("Cube", "Cube", { s, g -> ShapeEditor(Cube(g), s, g) }, FontAwesomeIcon.CUBE),
        EditorInfo("CubeEx", "CubeEx", { s, g -> ShapeEditor(CubeEx(g), s, g) }, FontAwesomeIcon.CUBES),
    )

    val editorHandler: EditorHandler

    init {
        val scope = super.scope as ScopeInfo
        editorsInfo.associate { it.name to it.editorFactory }.toMutableMap().apply {
            this[EmptyEditor::class.java.name] = EditorFactory { s, g -> EmptyEditor(s, g) }
            editorHandler = EditorHandler(this, scope.canvas)
        }
    }

    data class ScopeInfo(val canvas: Canvas) : Scope()
}