package com.github.erotourtes.drawing.editor

import com.github.erotourtes.drawing.shape.Line
import com.github.erotourtes.drawing.shape.Point
import com.github.erotourtes.drawing.shape.Rect
import javafx.scene.canvas.Canvas
import javafx.scene.input.MouseEvent

class PointEditor(canvas: Canvas, shapes: ShapesList) : Editor(canvas, shapes) {
    override val shape = Point(gc)

    override fun onMouseDragged(e: MouseEvent) {}

    override fun onMousePressed(e: MouseEvent) {
        dm.setEnd(e.x, e.y)
        super.onMousePressed(e)
        shape.draw(dm)
    }

    init {
        listenToEvents()
    }
}

class LineEditor(canvas: Canvas, shapes: ShapesList) : Editor(canvas, shapes) {
    override val shape = Line(gc)

    init {
        listenToEvents()
    }
}

class RectEditor(canvas: Canvas, shapes: ShapesList) : Editor(canvas, shapes) {
    override val shape = Rect(gc)
    override fun drawShowLine() {}

    init {
        listenToEvents()
    }
}
