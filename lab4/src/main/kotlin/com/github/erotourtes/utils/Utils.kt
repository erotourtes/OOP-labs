package com.github.erotourtes.utils

import com.github.erotourtes.drawing.editor.Editor
import com.github.erotourtes.drawing.editor.ShapesList
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import javafx.scene.canvas.GraphicsContext
import tornadofx.*

fun interface EditorFactory {
    fun create(shapes: ShapesList, gc: GraphicsContext): Editor
}

data class EditorInfo(
    val name: String,
    val tooltip: String,
    val editorFactory: EditorFactory,
    // [icons](https://fontawesome.com/v4/icons/)
    var icon: FontAwesomeIcon? = null,
)

const val g = 22 + 1
const val n = 100 + g

class PopupView : Fragment("Mode selection check") {
    private val action = super.scope as ScopeInfo

    override val root = vbox {
        style { prefWidth = 250.px }
        label("You clicked on ${action.name}")
        button("Close").action { close() }
    }

    data class ScopeInfo(val name: String) : Scope()
}