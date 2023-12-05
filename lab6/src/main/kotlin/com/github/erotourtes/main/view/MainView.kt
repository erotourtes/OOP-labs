package com.github.erotourtes.main.view

import com.github.erotourtes.data.MainModel
import com.github.erotourtes.data.MainState
import com.github.erotourtes.utils.*
import com.github.erotourtes.utils.EventEmitter
import com.github.erotourtes.utils.process_stream.ProcessReceiver
import com.github.erotourtes.utils.process_stream.ProcessSender
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
import javafx.stage.FileChooser
import javafx.stage.StageStyle
import tornadofx.*
import java.io.File
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty0

class MainController : Controller(), Closable {
    private var state = MainState()
    private val model: MainModel by inject()
    private val pathsModel: PathsModel by inject()
    private var program1: ProcessState? = null
    private var program2: ProcessState? = null

    init {
        model.item = state

        pathsModel.path1.onChange { program1?.close() }
        pathsModel.path2.onChange { program2?.close() }
    }

    fun showDialog() {
        val dialog = find<DialogView>()
        dialog.openModal(stageStyle = StageStyle.UTILITY)
    }

    fun send() {
        if (!isAlive(program1)) initChildProcessProgram1()
        program1?.sender?.send(MessageType.DATA, state.toString())
    }

    fun chooseFile(property: KProperty0<SimpleStringProperty>) {
        val file = chooseFile("Choose file", arrayOf(FileChooser.ExtensionFilter("JAR", "*.jar")))
        if (file.isNotEmpty()) {
            val path = file.first().absolutePath
            property.get().value = path
            pathsModel.commit()
        }
    }

    override fun close() {
        Logger.log("dispose")
        program1?.close()
        program2?.close()
    }

    private fun initChildProcessProgram1() {
        Logger.log("child process 1 init")
        val path = pathsModel.path1.value

        runJar(path, ::program1)

        program1!!.ee.subscribe(MessageType.DATA) {
            val items = ListConverter.toList<Double>(it, String::toDouble)
            if (items.isEmpty()) return@subscribe

            if (!isAlive(program2)) initChildProcessProgram2()
            program2?.sender?.send(MessageType.DATA, ListConverter.toString(items))
        }
    }

    private fun initChildProcessProgram2() {
        Logger.log("child process 2 init")
        val path = pathsModel.path2.value
        runJar(path, ::program2)
    }

    private fun isAlive(program: ProcessState?) = program?.alive ?: false

    private fun runJar(path: String, property: KMutableProperty0<ProcessState?>) {
        runCatching {
            val file = File(path)
            runJarFile(file)?.let {
                property.get()?.close()
                property.set(ProcessState(it, ProcessSender(it, EventEmitter.getFormatter()), ProcessReceiver(it)))
            }
        }.onFailure {
            Logger.log("runJar: ${it.message}", Logger.InfoType.ERROR)
        }
    }
}

class MainView : View("Main") {
    private val ctrl: MainController by inject()
    private val pathsModel: PathsModel by inject()

    override val root = vbox {
        menubar {
            menu("Lab") {
                item("Show dialog").action(ctrl::showDialog)
            }
        }

        borderpane {
            center = vbox {
                alignment = Pos.BASELINE_CENTER
                button("RUN") {
                    disableWhen(pathsModel.path1.isEmpty or pathsModel.path2.isEmpty)
                    action { ctrl.send() }
                }
            }

            bottom = form {
                fieldset {
                    field("program 1") {
                        label().textProperty().bind(pathsModel.path1.stringBinding {
                            "Path: ${formatPath(it)}"
                        })
                        button("Choose file").action {
                            ctrl.chooseFile(pathsModel::path1)
                        }
                    }
                    field("program 2") {
                        label().textProperty().bind(pathsModel.path2.stringBinding {
                            "Path: ${formatPath(it)}"
                        })
                        button("Choose file").action {
                            ctrl.chooseFile(pathsModel::path2)
                        }
                    }
                }
            }
        }
    }
}