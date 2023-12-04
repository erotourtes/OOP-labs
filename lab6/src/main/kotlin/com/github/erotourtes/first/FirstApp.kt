package com.github.erotourtes.first

import com.github.erotourtes.data.MainModel
import com.github.erotourtes.data.MainState
import com.github.erotourtes.utils.*
import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import tornadofx.*
import java.io.File

class FirstApp : App(FirstView::class)

class FirstController : Controller() {
    private val pReceiver = SelfInputStreamReceiver()
    private var pSender: ProcessSender? = null
    private var state = MainState()

    val model: MainModel by inject()
    val randoms: ObservableList<Double> = FXCollections.observableArrayList()

    init {
        pReceiver.run()

        pReceiver.inputMessage.addListener { _, _, newMsg ->
            runNotify("First App read message : $newMsg")
            if (newMsg == DESTROY || newMsg == null) {
                Platform.exit()
                return@addListener
            }

            val newState = MainState.fromString(newMsg) ?: return@addListener
            state = newState
            model.item = state

            regenerateDiapason()

            // TODO: refactor
            pSender?.writeMessage(randoms.joinToString(separator = ","))
        }
    }

    init {
        Runtime.getRuntime().addShutdownHook(Thread {
            dispose()
        })
    }

    init {
        // TODO: refactor
        val path =
            "/home/sirmax/Files/Documents/projects/kotlin/oop-labs-creating-last-step/lab6/out/artifacts/Second_jar/tornadofx-maven-project.jar"
        runJarFile(File(path))?.let {
            pSender = ProcessSender(it)
        }
    }

    private fun regenerateDiapason() {
        val (n, min, max) = state

        randoms.clear()
        for (i in 0 until n)
            randoms.add(min + (max - min) * Math.random())
    }

    private fun dispose() {
        pReceiver.close()
        pSender?.let {
            runNotify("First App write message : $DESTROY")
            it.writeMessage(DESTROY)
            it.close()
            it.process.destroy()
        }
    }
}

class FirstView : View("First View") {
    private val ctrl: FirstController by inject()

    override val root = vbox {
        label("n").bind(ctrl.model.nProp.stringBinding { "n = $it" })
        label("min").bind(ctrl.model.minProp.stringBinding { "min = $it" })
        label("max").bind(ctrl.model.maxProp.stringBinding { "max = $it" })

        listview(ctrl.randoms)
    }
}