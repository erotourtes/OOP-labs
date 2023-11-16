package com.github.erotourtes.view

import com.github.erotourtes.drawing.shape.EmptyShape
import com.github.erotourtes.drawing.shape.Shape
import com.github.erotourtes.drawing.shape.Shape.ShapeModel
import com.github.erotourtes.utils.shapeStatesToJSON
import javafx.beans.property.SimpleStringProperty
import javafx.beans.value.ObservableValue
import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import javafx.geometry.Pos
import javafx.scene.control.TableView
import javafx.stage.FileChooser
import javafx.stage.StageStyle
import tornadofx.*
import java.io.File

class Table<E, S>(
    private val data: ObservableList<E>,
    private val columnsData: List<Pair<String, (E) -> ObservableValue<S>>>,
) {

    var onUserSelectCb: (E) -> Unit = {}

    val root
        get() = TableView<E>().apply {
            columnsData.forEach {
                val (name, gerObservable) = it
                column(name) { value -> gerObservable(value.value) }
            }

            items = data
            onUserSelect(1, onUserSelectCb)
        }
}

class Form : View("Edit") {
    private val model by inject<CanvasModel>()
    private val shapeModel by inject<ShapeModel>()

    override val root = form {
        val editorHandler = model.eh
        hiddenWhen(shapeModel.isEmptyShape)
        fieldset("Selected Shape") {
            textProperty.bind(shapeModel.itemProperty.stringBinding { "Selected Shape ${it?.javaClass?.simpleName ?: ""}" })

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

class TableView : View("Table") {
    private val ctrl by inject<TableController>()
    private val shapeModel by inject<ShapeModel>()

    private val columnsData = listOf(
        "x1" to { shape: Shape -> shape.x1Prop },
        "y1" to { shape: Shape -> shape.y1Prop },
        "x2" to { shape: Shape -> shape.x2Prop },
        "y2" to { shape: Shape -> shape.y2Prop },
    )

    private val table = Table(ctrl.data, columnsData).apply {
        onUserSelectCb = {
            find<Form>().openModal(
                stageStyle = StageStyle.UTILITY,
                escapeClosesWindow = true,
                owner = this@TableView.currentWindow
            )

            ctrl.highlight(it)
            shapeModel.item = it
        }
    }

    override val root = borderpane {
        center = table.root
        bottom = hbox {
            button("Select File") { action { ctrl.selectFile() } }
            checkbox("Auto Save to file") {
                bind(ctrl.fileNameProp.stringBinding { str -> "Auto save to file ${str ?: "none"}" })
                action { ctrl.autoSave(isSelected) }
            }
        }
    }
}