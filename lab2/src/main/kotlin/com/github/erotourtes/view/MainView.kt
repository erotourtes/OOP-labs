package com.github.erotourtes.view

import com.github.erotourtes.app.*
import com.github.erotourtes.drawing.CanvasPane
import com.github.erotourtes.drawing.editor.*
import com.github.erotourtes.utils.MenuItemInfo
import javafx.scene.control.ToggleGroup
import tornadofx.*


class MainView : View("Lab2") {
    private val canvas: CanvasPane = CanvasPane()
    private var shapes = ShapesList(n)

    override val root = borderpane {
        top = MenuBar(createMenu())
        center = canvas
    }

    private fun createMenu(): List<MenuItemInfo> {
        val group = ToggleGroup()
        val canvas = this.canvas.canvas
        val menuList = listOf(
            MenuItemInfo("точка", { PointEditor(canvas, shapes) }, group, true),
            MenuItemInfo("лінія", { LineEditor(canvas, shapes) }, group),
            MenuItemInfo("прямокутник", { RectEditor(canvas, shapes) }, group),
            MenuItemInfo("еліпс", {}, group),
        )

        menuList.find { it.selected }?.action?.let { it() }

        return menuList
    }
}