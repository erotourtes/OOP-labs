package com.github.erotourtes.view

import com.github.erotourtes.drawing.shape.EmptyShape
import com.github.erotourtes.drawing.shape.Shape
import com.github.erotourtes.drawing.shape.Shape.ShapeModel
import com.github.erotourtes.utils.shapeStatesToJSON
import javafx.beans.property.SimpleStringProperty
import javafx.collections.ListChangeListener
import javafx.geometry.Pos
import javafx.stage.FileChooser
import javafx.stage.StageStyle
import tornadofx.*
import java.io.File

class Form : View("Edit") {
    private val model by inject<CanvasModel>()
    private val shapeModel by inject<ShapeModel>()

    override val root = form {
        val editorHandler = model.eh
        hiddenWhen(shapeModel.isEmptyShape)
        fieldset("Selected Shape") {
            hbox {
                style { alignment = Pos.CENTER }
                button("Save") {
                    enableWhen(shapeModel.dirty)
                    action {
                        shapeModel.commit()
                        with(editorHandler.editor) {
                            redraw()
                            highlight(shapeModel.item)
                        }
                    }
                }
                button("Reset") { action { shapeModel.rollback() } }
                button("Delete") {
                    action {
                        with(editorHandler.editor) {
                            shapeModel.item?.let {
                                model.sl.remove(it)
                                redraw()
                            }
                        }

                        this@Form.close()
                    }
                }
                button("Close") {
                    action {
                        shapeModel.item = EmptyShape
                        this@Form.close()
                    }
                }
            }
            field("x1: ") { textfield(shapeModel.x1) }
            field("x2: ") { textfield(shapeModel.x2) }
            field("y1: ") { textfield(shapeModel.y1) }
            field("y2: ") { textfield(shapeModel.y2) }
        }
    }
}

class TableController : Controller() {
    private val model by inject<CanvasModel>()
    private var file: File? = null
    private val listener = ListChangeListener<Shape> {
        file?.writeText(shapeStatesToJSON(model.sl.getStatesList()))
    }

    var fileNameProp = SimpleStringProperty()
    private var fileName by fileNameProp

    val data get() = model.sl.getObservableList()

    fun highlight(shape: Shape) {
        model.eh.editor.highlight(shape)
    }

    fun selectFile() {
        file = getFile()
        fileName = file?.name ?: "No file selected"
    }

    fun autoSave(isSelected: Boolean) {
        if (isSelected) {
            data.addListener(listener)
            listener.onChanged(null)
        } else data.removeListener(listener)
    }

    private fun getFile() = chooseFile(
        "Choose file...",
        filters = arrayOf(
            FileChooser.ExtensionFilter("JSON", "*.json"),
            FileChooser.ExtensionFilter("ALL", "*.*"),
        ),
        mode = FileChooserMode.Single,
    ).firstOrNull()
}

class Table : View("Table") {
    private val ctrl by inject<TableController>()
    private val shapeModel by inject<ShapeModel>()

    override val root = borderpane {
        center = tableview<Shape> {
            column("x1") { it.value.x1Prop }
            column("y1") { it.value.y1Prop }
            column("x2") { it.value.x2Prop }
            column("y2") { it.value.y2Prop }

            items = ctrl.data
            onUserSelect(1) {
                find<Form>().openModal(
                    stageStyle = StageStyle.UTILITY,
                    escapeClosesWindow = true,
                    owner = this@Table.currentWindow
                )

                ctrl.highlight(it)
                shapeModel.item = it
            }
//            shapeModel.rebindOnChange(this) { item = it }
        }

        bottom = hbox {
            button("Select File") { action { ctrl.selectFile() } }
            checkbox("Auto Save to file: ") { action { ctrl.autoSave(isSelected) } }
            label { bind(ctrl.fileNameProp) }
        }
    }
}