package com.github.erotourtes.first

import com.github.erotourtes.data.MainModel
import com.github.erotourtes.data.MainState
import com.github.erotourtes.utils.*
import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import tornadofx.*
import java.lang.management.ManagementFactory

class FirstApp : App(FirstView::class) {
    override fun init() {
        super.init()
        val pid = ManagementFactory.getRuntimeMXBean().name
        Logger.preMessage = "FirstApp($pid)"
    }

    override fun stop() {
        Logger.log("stop method")
        find<FirstController>().dispose()
        Logger.log("stop method end", Logger.InfoType.WARNING)
    }
}

class FirstController : Controller() {
    private val pReceiver = SelfInputStreamReceiver()
    private val pSender = SelfOutputStreamSender()
    private var state = MainState()

    val randoms: ObservableList<Double> = FXCollections.observableArrayList()

    init {
        pReceiver.inputMessage.addListener { _, _, newMsg ->
            if (newMsg == EMPTY) return@addListener
            if (newMsg == DESTROY) {
                Platform.exit()
                return@addListener
            }

            state = MainState.fromString(newMsg) ?: return@addListener
            regenerateDiapason()

            pSender.send(List::class.java, ListConverter.toString(randoms))
        }
    }

    private fun regenerateDiapason() {
        val (n, min, max) = state

        randoms.clear()
        for (i in 0 until n)
            randoms.add(min + (max - min) * Math.random())
    }

    fun dispose() {
        Logger.log("dispose")
        pReceiver.close()
    }
}

class FirstView : View("First View") {
    private val ctrl: FirstController by inject()

    override val root = vbox {
        listview(ctrl.randoms)
    }
}