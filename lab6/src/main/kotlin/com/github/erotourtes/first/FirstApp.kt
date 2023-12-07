package com.github.erotourtes.first

import com.github.erotourtes.data.MainState
import com.github.erotourtes.utils.*
import com.github.erotourtes.utils.EventEmitter
import com.github.erotourtes.utils.self_stream.SelfInputStreamReceiver
import com.github.erotourtes.utils.self_stream.SelfOutputStreamSender
import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.layout.Priority
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
        find<FirstController>().close()
        Logger.log("stop method end", Logger.InfoType.WARNING)
        super.stop()
    }
}

class FirstController : Controller(), Closable {
    private val ee = EventEmitter(SelfInputStreamReceiver())
    private val pSender = SelfOutputStreamSender(EventEmitter.getFormatter())
    private var state = MainState()

    val randoms: ObservableList<Double> = FXCollections.observableArrayList()

    init {
        ee.subscribe(MessageType.DESTROY) { Platform.exit() }
        ee.subscribe(MessageType.DATA) {
            runCatching {
                Logger.log("INPUT: $it")
                state = MainState.fromString(it) ?: return@subscribe
                regenerateDiapason()

                pSender.send(ListConverter.toString(randoms))
            }.onFailure {
                Logger.log("FirstController: ${it.message}", Logger.InfoType.ERROR)
                Platform.exit()
            }
        }
    }

    private fun regenerateDiapason() {
        val (n, min, max) = state

        randoms.clear()
        for (i in 0 until n)
            randoms.add(min + (max - min) * Math.random())
    }

    override fun close() {
        Logger.log("dispose")
        pSender.send(MessageType.ON_DESTROY)
        ee.close()
    }
}

class FirstView : View("First View") {
    private val ctrl: FirstController by inject()

    override val root = vbox {
        listview(ctrl.randoms) { vgrow = Priority.ALWAYS }
    }
}