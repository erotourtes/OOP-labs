package com.github.erotourtes.view

import com.github.erotourtes.drawing.EditorHandler
import com.github.erotourtes.drawing.editor.*
import com.github.erotourtes.styles.ToolbarStyles
import com.github.erotourtes.utils.ToolbarItemInfo
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView
import javafx.beans.value.ChangeListener
import javafx.scene.control.ToggleGroup
import javafx.scene.layout.HBox
import tornadofx.*

class ToolBar(items: List<ToolbarItemInfo>, editorHandler: EditorHandler) : HBox() {
    private val group = ToggleGroup()
    private val listener: ChangeListener<Editor> = ChangeListener { _, _, newValue -> listenToChanges(newValue) }

    init {
        addClass(ToolbarStyles.toolbar)

        items.forEach {
            togglebutton {
                tooltip(it.tooltip)
                addClass(ToolbarStyles.iconButton)
                add(FontAwesomeIconView(it.icon).apply { addClass(ToolbarStyles.icon) })
                toggleGroup = group
                isSelected = false
                userData = it
                action { it.action(this) }
            }
        }

        editorHandler.listenToChanges(listener)
    }

    private fun listenToChanges(newValue: Editor) {
        group.toggles.forEach {
            val userData = it.userData as ToolbarItemInfo
            it.isSelected = userData.kotlinClass == newValue.javaClass
        }
    }

    companion object {
        fun create(editorHandler: EditorHandler): ToolBar {
            // [icons](https://fontawesome.com/v4/icons/)
            val objects = listOf(
                ToolbarItemInfo(
                    tooltip = "Dot", icon = FontAwesomeIcon.DOT_CIRCLE_ALT, kotlinClass = PointEditor::class.java
                ),
                ToolbarItemInfo(
                    tooltip = "Line", icon = FontAwesomeIcon.MINUS, kotlinClass = LineEditor::class.java
                ),
                ToolbarItemInfo(
                    tooltip = "Rectangle", icon = FontAwesomeIcon.SQUARE, kotlinClass = RectEditor::class.java
                ),
                ToolbarItemInfo(
                    tooltip = "Ellipse", icon = FontAwesomeIcon.CIRCLE_ALT, kotlinClass = EllipseEditor::class.java
                ),
            ).map {
                it.action =
                    { btn -> editorHandler.useEditor(if (btn.isSelected) it.kotlinClass else EmptyEditor::class.java) }
                it
            }

            return ToolBar(objects, editorHandler)
        }
    }
}