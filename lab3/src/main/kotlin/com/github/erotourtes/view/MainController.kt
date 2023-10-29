package com.github.erotourtes.view

import com.github.erotourtes.drawing.EditorHandler
import com.github.erotourtes.drawing.editor.*
import com.github.erotourtes.utils.ShapeInfo
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import javafx.scene.canvas.Canvas
import tornadofx.*

class MainController : Controller() {
    val shapesInfo = listOf(
        ShapeInfo("Dot", "Dot", PointEditor::class.java, FontAwesomeIcon.DOT_CIRCLE_ALT),
        ShapeInfo("Line", "Line", LineEditor::class.java, FontAwesomeIcon.MINUS),
        ShapeInfo("Rectangle", "Rectangle", RectEditor::class.java, FontAwesomeIcon.SQUARE),
        ShapeInfo("Ellipse", "Ellipse", EllipseEditor::class.java, FontAwesomeIcon.CIRCLE_ALT),
    )

    val editorHandler: EditorHandler

    init {
        val scope = super.scope as ScopeInfo
        editorHandler = EditorHandler(scope.canvas)
        editorHandler.useEditor(EmptyEditor::class.java)
    }

    data class ScopeInfo(val canvas: Canvas) : Scope()
}