package com.github.erotourtes.utils

import com.github.erotourtes.drawing.editor.Editor
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import javafx.scene.control.ToggleGroup

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