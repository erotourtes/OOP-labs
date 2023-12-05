package com.github.erotourtes.second

import com.github.erotourtes.utils.*
import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import tornadofx.*
import java.lang.management.ManagementFactory


class SecondApp : App(SecondView::class) {
    override fun init() {
        super.init()
        val pid = ManagementFactory.getRuntimeMXBean().name
        Logger.preMessage = "SecondApp($pid)"
    }

    override fun stop() {
        Logger.log("stop method")
        find<SecondController>().dispose()
        Logger.log("stop method end", Logger.InfoType.WARNING)
        super.stop()
    }
}

class SecondController : Controller() {
    private val pReceiver = SelfInputStreamReceiver()
    val randoms: ObservableList<Double> = FXCollections.observableArrayList()

    init {
        pReceiver.inputMessage.addListener { _, _, newValue ->
            if (newValue == EMPTY || newValue.isEmpty()) return@addListener
            if (newValue == DESTROY) {
                Platform.exit()
                return@addListener
            }

            val list = ListConverter.toList(newValue, String::toDouble).sorted()
            randoms.clear()
            randoms.addAll(list)
        }
    }

    fun dispose() {
        Logger.log("dispose")
        pReceiver.close()
    }
}

class SecondView : View("Second View") {
    private val ctrl by inject<SecondController>()

    override val root = vbox {
        listview(ctrl.randoms)
    }
}