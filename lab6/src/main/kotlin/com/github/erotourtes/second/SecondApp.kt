package com.github.erotourtes.second

import com.github.erotourtes.utils.DESTROY
import com.github.erotourtes.utils.SelfInputStreamReceiver
import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import tornadofx.*

class SecondApp : App(SecondView::class) {
    override fun stop() {
        find<SecondController>().dispose()
        super.stop()
    }
}

class SecondController : Controller() {
    private val pReceiver = SelfInputStreamReceiver()
    val randoms: ObservableList<Double> = FXCollections.observableArrayList()

    init {
        pReceiver.run()

        pReceiver.inputMessage.addListener { _, _, newValue ->
            if (newValue == DESTROY) Platform.exit()

            val list = if (newValue.isEmpty()) emptyList() else newValue.splitToSequence(",")
                .map { it.trimIndent().toDouble() }.sorted().toList()
            randoms.clear()
            randoms.addAll(list)
        }
    }

    fun dispose() {
        pReceiver.close()
    }
}

class SecondView : View("Second View") {
    private val ctrl by inject<SecondController>()

    override val root = vbox {
        listview(ctrl.randoms)
    }

    override fun onDelete() {
        ctrl.dispose()
        super.onDelete()
    }
}