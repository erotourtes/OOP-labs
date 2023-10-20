package com.github.erotourtes.view

import com.github.erotourtes.drawing.EditorHandler
import com.github.erotourtes.drawing.editor.Editor
import com.github.erotourtes.styles.ToolbarStyles
import com.github.erotourtes.utils.ToolbarItemInfo
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
                action { editorHandler.useEditor(it.kotlinClass) }
            }
        }

        editorHandler.listenToChanges(listener)
    }

    private fun listenToChanges(newValue: Editor) {
        val editorClass = newValue::class.java
        group.toggles.forEach {
            val userData = it.userData as ToolbarItemInfo
            if (userData.kotlinClass == editorClass) it.isSelected = true
        }
    }
}