package com.github.erotourtes.drawing.editor

import com.github.erotourtes.drawing.shape.*
import javafx.scene.canvas.GraphicsContext
import javafx.scene.input.MouseEvent

class PointEditor(shapes: ShapesList, gc: GraphicsContext) : Editor(shapes, gc) {
    override val shape = Point(gc)

    override fun onMouseDragged(e: MouseEvent) {}

    override fun onMousePressed(e: MouseEvent) {
        dm.setEnd(e.x, e.y)
        super.onMousePressed(e)
        shape.setDm(dm)
    }

    override fun onMouseReleased(e: MouseEvent) {
        shapes.add(shape.copy())
        redraw()
    }

    override fun previewLine() {}
}

class EmptyEditor(shapes: ShapesList, gc: GraphicsContext) : Editor(shapes, gc) {
    override val shape = object : Shape(gc) {
        override fun draw() {}
    }

    override fun onMouseDragged(e: MouseEvent) {}
    override fun onMousePressed(e: MouseEvent) {}
    override fun onMouseReleased(e: MouseEvent) {}
    override fun previewLine() {}
}

class ShapeEditor(override val shape: Shape, shapes: ShapesList, gc: GraphicsContext) : Editor(shapes, gc)