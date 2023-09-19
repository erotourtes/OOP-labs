package com.github.erotourtes.task2

import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.control.ListView
import tornadofx.*

class ListController(
    private val fragment: Fragment,
    private val observable: SimpleStringProperty
) : Controller() {
    val items: ObservableList<String> = FXCollections.observableArrayList("IM-21", "IM-22", "IM-23", "IM-24")
    private var selectedMsg = ""

    fun handleChange(listview: ListView<String>) {
        listview.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            selectedMsg = newValue
        }
    }

    fun handleBtnYesChange() {
        observable.value = this.selectedMsg
    }

    fun handleBtnCancelChange() {
        observable.value = ""
        fragment.close()
    }
}

class Window : Fragment("Window") {
    val message: SimpleStringProperty by param()
    private val controller = ListController(this, message)

    override val root = vbox {
        listview(controller.items) { controller.handleChange(this) }
        hbox {
            button("Yes") { action(controller::handleBtnYesChange) }
            button("Cancel") { action(controller::handleBtnCancelChange) }
        }
    }
}