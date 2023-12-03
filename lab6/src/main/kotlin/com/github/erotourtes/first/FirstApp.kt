package com.github.erotourtes.first

import com.github.erotourtes.data.MainModel
import com.github.erotourtes.data.MainState
import com.github.erotourtes.process_communicators.ProcessSelfReceiver
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import tornadofx.*

class FirstApp : App(FirstView::class) {
    override fun stop() {
        find<FirstController>().dispose()
        super.stop()
    }
}

class FirstController : Controller() {
    private val pc = ProcessSelfReceiver()
    private var state = MainState()

    val model: MainModel by inject()
    val randoms: ObservableList<Double> = FXCollections.observableArrayList<Double>()

    init {
        pc.run()

        pc.inputMessage.addListener { _, _, newValue ->
            val newState = MainState.fromString(newValue) ?: return@addListener
            state = newState
            model.item = state

            regenerateDiapason()
        }
    }

    private fun regenerateDiapason() {
        val min = state.minValue
        val max = state.maxValue
        val n = state.n

        randoms.clear()
        for (i in 0 until n)
            randoms.add(min + (max - min) * Math.random())
    }

    fun dispose() {
        pc.close()
    }
}

class FirstView : View("First View") {
    private val ctrl: FirstController by inject()

    override val root = vbox {
        label("n").bind(ctrl.model.nValueProp.stringBinding { "n = $it" })
        label("min").bind(ctrl.model.minValueProp.stringBinding { "min = $it" })
        label("max").bind(ctrl.model.maxValueProp.stringBinding { "max = $it" })

        listview(ctrl.randoms)
    }

    override fun onDelete() {
        ctrl.dispose()
        super.onDelete()
    }
}