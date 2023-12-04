package com.github.erotourtes.second

import com.github.erotourtes.utils.DESTROY
import com.github.erotourtes.utils.EMPTY
import com.github.erotourtes.utils.SelfInputStreamReceiver
import com.github.erotourtes.utils.runNotify
import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import tornadofx.*


class SecondApp : App(SecondView::class) {
    override fun stop() {
        runNotify("SecondApp(stop)")
        find<SecondController>().dispose()
    }
}

class SecondController : Controller() {
    private val pReceiver = SelfInputStreamReceiver()
    val randoms: ObservableList<Double> = FXCollections.observableArrayList()

    init {
        pReceiver.inputMessage.addListener { _, _, newValue ->
            runNotify("SecondApp(message): $newValue")
            if (newValue == EMPTY) return@addListener
            if (newValue == DESTROY || newValue == null) {
                /*
                Somehow when i close the process using `kill` the stop() hook is not fired up.
                instead the parent sends through the stream null
                */
                Platform.exit()
                return@addListener
            }

            val list = if (newValue.isEmpty()) emptyList() else newValue.splitToSequence(",")
                .map { it.trimIndent().toDouble() }.sorted().toList()
            randoms.clear()
            randoms.addAll(list)
        }
    }

    fun dispose() {
        runNotify("SecondApp(destroy)")
        pReceiver.close()
    }
}

class SecondView : View("Second View") {
    private val ctrl by inject<SecondController>()

    override val root = vbox {
        listview(ctrl.randoms)
    }
}