package com.github.erotourtes.task2

import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.control.ListView
import tornadofx.*

class ListController : Controller() {
    val items: ObservableList<String> = FXCollections.observableArrayList("IM-21", "IM-22", "IM-23", "IM-24")
    private var message = ""

    fun handleChange(listview: ListView<String>) {
        listview.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            message = newValue
        }
    }
    fun handleBtnChange(message: SimpleStringProperty) {
        message.value = this.message
    }
}

class Window : Fragment("Window") {
    val message: SimpleStringProperty by param()
    private val controller = ListController()

    override val root = vbox {
        listview(controller.items) { controller.handleChange(this) }
        hbox {
            button("Show") { action { controller.handleBtnChange(message) } }
            button("Cancel") { action {
                message.value = ""
                close()
            } }
        }
    }
}