package com.github.erotourtes.view

import com.github.erotourtes.drawing.editor.EmptyEditor
import com.github.erotourtes.styles.ToolbarStyles
import com.github.erotourtes.utils.EditorInfo
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView
import javafx.scene.control.SpinnerValueFactory
import javafx.scene.control.ToggleButton
import javafx.scene.control.ToggleGroup
import javafx.scene.layout.BorderPane
import javafx.scene.paint.Color
import javafx.stage.StageStyle
import tornadofx.*

class ToolBarController : Controller() {
    val detached
        get() = isDetached
    private val model by inject<CanvasModel>()
    private val editorsInfoModel by inject<EditorsInfoModel>()
    private val group = ToggleGroup()
    private lateinit var toolBar: ToolBar
    private var isDetached = false

    fun setView(view: ToolBar) {
        this.toolBar = view
    }

    fun create() = editorsInfoModel.editorsInfo.value.map {
        ToggleButton().apply {
            tooltip(it.tooltip)
            addClass(ToolbarStyles.iconButton)
            add(FontAwesomeIconView(it.icon).apply { addClass(ToolbarStyles.icon) })
            toggleGroup = group
            isSelected = false
            userData = it
            action {
                val eh = model.eh

                if (this.isSelected) eh.use(it.pair)
                else eh.use(Pair(EmptyEditor.shape.javaClass, EmptyEditor))
            }
        }
    }

    fun listenToEditorChange() {
        val editorHandler = model.eh

        editorHandler.listenToChanges { _, _, _ ->
            group.toggles.forEach {
                val userData = it.userData as EditorInfo
                it.isSelected = editorHandler.isCurShapeActive(userData.pair)
            }
        }
    }

    fun undo() = model.h.undo()
    fun redo() = model.h.redo()

    fun toggle() = if (isDetached) attach() else detach()

    fun changeMainColor(color: Color) = model.cc.changeFillColor(color)

    val mainColorProp = model.cc.fillColorProp
    val auxColorProp = model.cc.strokeColorProp

    fun changeAuxColor(color: Color) = model.cc.changeStrokeColor(color)

    fun changeStroke(width: Double) = model.cc.changeStrokeWidth(width)

    fun swapColors() {
        val aux = auxColorProp.value
        val main = mainColorProp.value
        model.cc.also {
            it.changeStrokeColor(main)
            it.changeFillColor(aux)
        }
    }

    private fun attach() {
        isDetached = false
        val center = find<MainView>().root.center as BorderPane
        toolBar.close()
        center.top = toolBar.root
    }

    private fun detach() {
        isDetached = true
        toolBar.removeFromParent()
        toolBar.openWindow(StageStyle.UTILITY)
    }
}

class ToolBar : View() {
    private val ctrl by inject<ToolBarController>()

    override fun onDock() {
        ctrl.setView(this)
        ctrl.listenToEditorChange()
    }

    override val root = flowpane {
        addClass(ToolbarStyles.toolbar)

        button {
            addClass(ToolbarStyles.iconButton)
            val detach = FontAwesomeIconView(FontAwesomeIcon.UNLINK)
            val attach = FontAwesomeIconView(FontAwesomeIcon.LINK)
            graphic = detach

            action {
                graphic = if (ctrl.detached) detach else attach
                ctrl.toggle()
            }
        }
        button {
            addClass(ToolbarStyles.iconButton)
            graphic = FontAwesomeIconView(FontAwesomeIcon.REPEAT)

            action { ctrl.redo() }
        }
        button {
            addClass(ToolbarStyles.iconButton)
            graphic = FontAwesomeIconView(FontAwesomeIcon.UNDO)

            action { ctrl.undo() }
        }

        label {
            style { borderWidth += box(0.px, 0.px, 0.px, 1.px); borderColor += box(c("#000000")) }
        }

        ctrl.create().forEach { add(it) }

        colorpicker {
            addClass(ToolbarStyles.iconButton)
            valueProperty().bindBidirectional(ctrl.mainColorProp)
            setOnAction { ctrl.changeMainColor(value) }
        }

        colorpicker {
            addClass(ToolbarStyles.iconButton)
            valueProperty().bindBidirectional(ctrl.auxColorProp)
            setOnAction { ctrl.changeAuxColor(value) }
        }

        button {
            addClass(ToolbarStyles.iconButton)
            graphic = FontAwesomeIconView(FontAwesomeIcon.EXCHANGE)

            action { ctrl.swapColors() }
        }

        spinner<Double> {
            addClass(ToolbarStyles.iconButton)
            valueFactory = SpinnerValueFactory.DoubleSpinnerValueFactory(1.0, 10.0, 1.0)
            valueProperty().addListener { _, _, new -> ctrl.changeStroke(new) }
        }
    }
}