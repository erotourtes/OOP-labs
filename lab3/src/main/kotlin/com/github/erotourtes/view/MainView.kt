package com.github.erotourtes.view

import com.github.erotourtes.drawing.CanvasHandler
import com.github.erotourtes.drawing.CanvasPane
import com.github.erotourtes.drawing.editor.*
import com.github.erotourtes.utils.MenuItemInfo
import javafx.scene.control.ToggleGroup
import tornadofx.*


class MainView : View("Lab2") {
    private val canvasHandler = CanvasHandler()

    override val root = borderpane {
        top = MenuBar(createMenu())
        center = CanvasPane(canvasHandler.canvas)
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