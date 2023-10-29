package com.github.erotourtes.utils

import com.github.erotourtes.drawing.editor.Editor
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import tornadofx.*

data class ShapeInfo(
    val name: String,
    val tooltip: String,
    val kotlinClass : Class<out Editor>,
    // [icons](https://fontawesome.com/v4/icons/)
    var icon: FontAwesomeIcon? = null,
)

fun getToCornerDimension(dm: Dimension): Dimension {
    val w = dm.width
    val h = dm.height
    val (cx, cy) = dm.getRaw().first

    val sX = cx - w
    val sY = cy - h

    return Dimension().setStart(sX, sY).setEnd(sX + w * 2, sY + h * 2)
}

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