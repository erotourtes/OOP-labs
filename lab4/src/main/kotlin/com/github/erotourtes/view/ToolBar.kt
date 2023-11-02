package com.github.erotourtes.view

import com.github.erotourtes.drawing.editor.EmptyEditor
import com.github.erotourtes.styles.ToolbarStyles
import com.github.erotourtes.utils.EditorInfo
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView
import javafx.scene.control.ToggleButton
import javafx.scene.control.ToggleGroup
import tornadofx.*

class ToolBarController : Controller() {
    private val model: CanvasModel by inject()
    private val group = ToggleGroup()

    fun create() = model.ei.map {
        ToggleButton().apply {
            tooltip(it.tooltip)
            addClass(ToolbarStyles.iconButton)
            add(FontAwesomeIconView(it.icon).apply { addClass(ToolbarStyles.icon) })
            toggleGroup = group
            isSelected = false
            userData = it
            action {
                model.eh.useEditor(
                    if (this.isSelected) it.name else EmptyEditor::class.java.name
                )
            }
        }
    }

    fun listenToEditorChange() {
        val editorHandler = model.eh

        editorHandler.listenToChanges { _, _, newValue ->
            group.toggles.forEach {
                val userData = it.userData as EditorInfo
                it.isSelected = userData.name == newValue
            }
        }
    }
}

class ToolBar : View() {
    private val ctrl: ToolBarController by inject(super.scope)

    override fun onDock() {
        ctrl.listenToEditorChange()
    }

    override val root = hbox {
        addClass(ToolbarStyles.toolbar)
        ctrl.create().forEach { add(it) }
    }
}