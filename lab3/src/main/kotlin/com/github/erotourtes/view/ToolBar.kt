package com.github.erotourtes.view

import com.github.erotourtes.styles.ToolbarStyles
import com.github.erotourtes.utils.ToolbarItemInfo
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView
import javafx.scene.control.ToggleGroup
import javafx.scene.layout.HBox
import tornadofx.*

class ToolBar(items: List<ToolbarItemInfo>) : HBox() {
    init {
        addClass(ToolbarStyles.toolbar)
        val group = ToggleGroup()
        items.forEach {
            togglebutton {
                val icon = FontAwesomeIconView(it.icon).apply { addClass(ToolbarStyles.icon) }
                add(icon)
                action { it.action() }
                tooltip(it.tooltip)
                addClass(ToolbarStyles.iconButton)
                toggleGroup = group
            }
        }

        group.toggles.forEach { it.isSelected = false }
    }
}