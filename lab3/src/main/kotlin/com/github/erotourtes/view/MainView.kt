package com.github.erotourtes.view

import com.github.erotourtes.drawing.EditorHandler
import com.github.erotourtes.drawing.CanvasPane
import com.github.erotourtes.drawing.editor.*
import com.github.erotourtes.utils.MenuItemInfo
import com.github.erotourtes.utils.ToolbarItemInfo
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import javafx.scene.canvas.Canvas
import javafx.scene.control.ToggleGroup
import tornadofx.*

class MainView : View("Lab3") {
    private val canvas = Canvas()
    private val editorHandler = EditorHandler(canvas)

    override val root = borderpane {
        top = MenuBar(createMenu())
        center = borderpane {
            top = ToolBar(createToolbar(), editorHandler)
            center = CanvasPane(canvas)
        }
    }

    // [icons](https://fontawesome.com/v4/icons/)
    private fun createToolbar(): List<ToolbarItemInfo> = listOf(
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
    )

    private fun createMenu(): List<MenuItemInfo> {
        val group = ToggleGroup()
        val menuList = listOf(
            MenuItemInfo("Dot", PointEditor::class.java),
            MenuItemInfo("Line", LineEditor::class.java),
            MenuItemInfo("Rectangle", RectEditor::class.java),
            MenuItemInfo("Ellipse", EllipseEditor::class.java),
        )

        menuList.forEach { it.group = group; it.action = { editorHandler.useEditor(it.kotlinClass) } }

        editorHandler.listenToChanges { _, _, newValue ->
            val editor = newValue::class.java

            group.toggles.forEach {
                val userData = it.userData as MenuItemInfo
                if (userData.kotlinClass == editor) it.isSelected = true
            }
        }

        return menuList
    }
}
