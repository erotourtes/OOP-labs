package com.github.erotourtes.utils

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import javafx.scene.control.ToggleGroup

data class MenuItemInfo(
    val name: String,
    val action: () -> Unit,
    val group: ToggleGroup? = null,
    var selected: Boolean = false
)

data class ToolbarItemInfo(
    val name: String,
    val tooltip: String,
    val action: () -> Unit,
    var selected: Boolean = false,
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
