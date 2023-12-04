package com.github.erotourtes.first

import com.github.erotourtes.data.MainModel
import com.github.erotourtes.data.MainState
import com.github.erotourtes.utils.*
import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import tornadofx.*
import java.io.File
import kotlin.system.exitProcess

class FirstApp : App(FirstView::class) {
    override fun stop() {
        runNotify("FirstApp(stop)")
        find<FirstController>().dispose()
    }
}

class FirstController : Controller() {
    private val pReceiver = SelfInputStreamReceiver()
    private var pSender: ProcessSender? = null
    private var state = MainState()

    val model: MainModel by inject()
    val randoms: ObservableList<Double> = FXCollections.observableArrayList()

    init {
        pReceiver.inputMessage.addListener { _, _, newMsg ->
            runNotify("FirstApp(read): $newMsg")
            if (newMsg == EMPTY) return@addListener
            if (newMsg == DESTROY || newMsg == null) {
                dispose()
                Platform.exit()
                return@addListener
            }

            val newState = MainState.fromString(newMsg) ?: return@addListener
            state = newState
            model.item = state

            regenerateDiapason()

            runNotify("FirstApp(check): $state ${pSender?.isAlive}")
            if (pSender == null || !pSender!!.isAlive) initChildProcess()
            pSender?.writeMessage(randoms.joinToString(separator = ","))
        }
    }

    init {
        Runtime.getRuntime().addShutdownHook(Thread {
            dispose()
        })
    }

    init {
        initChildProcess()
    }

    private fun initChildProcess() {
        runNotify("FirstApp(CHILDPROCESS)")
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

    fun dispose() {
        pSender?.let {
            runNotify("FirstApp(send): $DESTROY")
            it.writeMessage(DESTROY)
            it.close()
            it.process.destroy()
        }
        runNotify("FirstApp(DESTROY)")
        pReceiver.close()
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