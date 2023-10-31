package com.github.erotourtes.view

import com.github.erotourtes.drawing.EditorHandler
import com.github.erotourtes.drawing.editor.EmptyEditor
import com.github.erotourtes.styles.ToolbarStyles
import com.github.erotourtes.utils.EditorInfo
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView
import javafx.scene.Node
import javafx.scene.control.ToggleButton
import javafx.scene.control.ToggleGroup
import javafx.scene.layout.HBox
import tornadofx.*

class ToolBar(items: List<Node>) : HBox() {
    init {
        addClass(ToolbarStyles.toolbar)

        items.forEach { this += it }
    }

    companion object {
        fun create(editorHandler: EditorHandler, list: List<EditorInfo>): ToolBar {
            val group = ToggleGroup()

            editorHandler.listenToChanges { _, _, newValue ->
                group.toggles.forEach {
                    val userData = it.userData as EditorInfo
                    it.isSelected = userData.name == newValue
                }
            }

            val shapesUI = list.map {
                ToggleButton().apply {
                    tooltip(it.tooltip)
                    addClass(ToolbarStyles.iconButton)
                    add(FontAwesomeIconView(it.icon).apply { addClass(ToolbarStyles.icon) })
                    toggleGroup = group
                    isSelected = false
                    userData = it
                    action {
                        editorHandler.useEditor(
                            if (this.isSelected) it.name else EmptyEditor::class.java.name
                        )
                    }
                }
            }

            return ToolBar(shapesUI)
        }
    }
}