package com.github.erotourtes.drawing.editor

import com.github.erotourtes.drawing.shape.*
import com.github.erotourtes.utils.*
import javafx.scene.canvas.GraphicsContext
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color

class PointEditor(shapes: ShapesList, gc: GraphicsContext) : Editor(shapes, gc) {
    override val shape = Point(gc)

    override fun onMouseDragged(e: MouseEvent) {}

    override fun onMousePressed(e: MouseEvent) {
        dm.setEnd(e.x, e.y)
        super.onMousePressed(e)
        shape.draw(dm)
    }

    override fun onMouseReleased(e: MouseEvent) = shapes.add(shape.copy())
}

class LineEditor(shapes: ShapesList, gc: GraphicsContext) : Editor(shapes, gc) {
    override val shape = Line(gc)

    override fun drawShowLine() = gc.drawOnce {
        gc.stroke = Color.BLUE
        strokeLine(dm)
    }
}

class RectEditor(shapes: ShapesList, gc: GraphicsContext) : Editor(shapes, gc) {
    override val shape = Rect(gc)
    override fun drawShowLine()  = gc.drawOnce {
        gc.stroke = Color.BLUE
        strokeRect(dm)
    }
}

class EllipseEditor(shapes: ShapesList, gc: GraphicsContext) : Editor(shapes, gc) {
    override val shape = Ellipse(gc)
    override fun drawShowLine() = gc.drawOnce {
        gc.stroke = Color.BLUE
        strokeOval(getEllipseDimensions(dm))
    }
}
