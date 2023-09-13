package com.github.erotourtes.view

import com.github.erotourtes.task1.TaskView
import javafx.scene.layout.HBox
import tornadofx.*

class MenuBar : View("Menu") {
    val changeView: (Int) -> Unit

    init {
        val changeView = params["changeView"] as? (Int) -> Unit
            ?: throw IllegalArgumentException("changeView is null")
        this.changeView = changeView
    }

    override val root = menubar {
        menu("Tasks") {
            item("Do task 1", "Shortcut+1").action {
                changeView(1)
            }
            item("Do task 2", "Shortcut+2").action {
                changeView(2)
            }
        }
    }
}

class MainView : View("Lab 1") {
    private val firstView = TaskView()
    private val centerView = HBox()

    private val changeView = { i: Int ->
        when (i) {
            1 -> centerView.replaceChildren(TaskView().root)
            2 -> centerView.replaceChildren(com.github.erotourtes.task2.TaskView().root)
            else -> throw IllegalArgumentException("Unknown task number")
        }
    }

    private val menuBar: MenuBar by inject(params = mapOf("changeView" to changeView))

    override val root = borderpane {
        top = menuBar.root
        center = centerView.apply { add(firstView.root) }
    }
}