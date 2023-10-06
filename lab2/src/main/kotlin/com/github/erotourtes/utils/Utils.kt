package com.github.erotourtes.utils

import javafx.scene.control.ToggleGroup

data class MenuItemInfo(
    val name: String,
    val action: () -> Unit,
    val group: ToggleGroup? = null,
    var selected: Boolean = false
)

fun getEllipseDimensions(dm: Dimension): Dimension {
    val w = dm.width
    val h = dm.height
    val (cx, cy) = dm.getRaw().first

    val sX = cx - w
    val sY = cy - h

    return Dimension().setStart(sX, sY).setEnd(sX + w * 2, sY + h * 2)
}