package com.github.erotourtes.utils

import com.github.erotourtes.drawing.editor.Editor
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import javafx.scene.control.ToggleButton
import javafx.scene.control.ToggleGroup
import tornadofx.*

data class MenuItemInfo(
    val name: String,
    val kotlinClass : Class<out Editor>,
    var group: ToggleGroup? = null,
    var action: () -> Unit = {},
)

data class ToolbarItemInfo(
    val tooltip: String,
    val kotlinClass : Class<out Editor>,
    var icon: FontAwesomeIcon? = null,
    var action: (ToggleButton) -> Unit = {},
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