package com.github.erotourtes.view

import com.github.erotourtes.drawing.CanvasHandler
import com.github.erotourtes.drawing.CanvasPane
import com.github.erotourtes.drawing.editor.*
import com.github.erotourtes.styles.ToolbarStyles
import com.github.erotourtes.utils.MenuItemInfo
import com.github.erotourtes.utils.ToolbarItemInfo
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView
import javafx.scene.control.ToggleGroup
import tornadofx.*

class MainView : View("Lab3") {
    private val canvasHandler = CanvasHandler()

    override val root = borderpane {
        top = MenuBar(createMenu())
        center = borderpane {
            top = ToolBar(createToolbar())
            center = CanvasPane(canvasHandler.canvas)
        }
    }

    private fun createToolbar(): List<ToolbarItemInfo> {
        // [icons](https://fontawesome.com/v4/icons/)
        val toolbarList = listOf(
            ToolbarItemInfo(
                name = "Zoom in",
                tooltip = "Select Zoom in tool",
                selected = false,
                icon = FontAwesomeIcon.PLUS,
                action = { println("pressed Zoom in") },
            ),
            ToolbarItemInfo(
                name = "Zoom out",
                tooltip = "Select Zoom out tool",
                selected = false,
                icon = FontAwesomeIcon.MINUS,
                action = { println("pressed Zoom out") },
            ),
            ToolbarItemInfo(
                name = "Hand",
                tooltip = "Select Hand tool",
                selected = false,
                icon = FontAwesomeIcon.HAND_GRAB_ALT,
                action = { println("pressed Hand") },
            ),
            ToolbarItemInfo(
                name = "Layers",
                tooltip = "Select Layers tool",
                selected = false,
                icon = FontAwesomeIcon.LIST,
                action = { println("pressed Layers") },
            ),
            ToolbarItemInfo(
                name = "Help",
                tooltip = "Select Help tool",
                selected = false,
                icon = FontAwesomeIcon.QUESTION,
                action = { println("pressed Help") },
            )
        )

        toolbarList.find { it.selected }?.action?.let { it() }

        return toolbarList
    }

    private fun createMenu(): List<MenuItemInfo> {
        val group = ToggleGroup()
        val menuList = with(canvasHandler) {
            listOf(
                MenuItemInfo("точка", { useEditor<PointEditor>() }, group, true),
                MenuItemInfo("лінія", { useEditor<LineEditor>() }, group),
                MenuItemInfo("прямокутник", { useEditor<RectEditor>() }, group),
                MenuItemInfo("еліпс", { useEditor<EllipseEditor>() }, group),
            )
        }

        menuList.find { it.selected }?.action?.let { it() }

        return menuList
    }
}
