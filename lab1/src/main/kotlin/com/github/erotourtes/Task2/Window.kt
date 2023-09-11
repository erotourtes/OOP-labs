package com.github.erotourtes.Task2

import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.control.ListView
import tornadofx.*

class ListController : Controller() {
    val items: ObservableList<String> = FXCollections.observableArrayList("IM-21", "IM-22", "IM-23", "IM-24")

    fun handleChange(listview: ListView<String>, message: SimpleStringProperty) {
        listview.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            message.value = newValue
        }
    }
}

class Window : Fragment("Window") {
    val message: SimpleStringProperty by param()
    private val controller: ListController by inject()

    override val root = hbox {
        listview(controller.items) {
            controller.handleChange(this, message)
        }
    }
}