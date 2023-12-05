package com.github.erotourtes.main.view

import javafx.beans.property.SimpleStringProperty
import tornadofx.*

class PathsModel : ViewModel() {
    private val KEY_FIRST_PATH = "path1"
    private val KEY_SECOND_PATH = "path2"

    val path1 = bind { SimpleStringProperty("", "", config.string(KEY_FIRST_PATH).orEmpty()) }
    val path2 = bind { SimpleStringProperty("", "", config.string(KEY_SECOND_PATH).orEmpty()) }

    override fun onCommit() {
        with(config) {
            set(KEY_FIRST_PATH to path1.value)
            set(KEY_SECOND_PATH to path2.value)
            save()
        }
    }
}