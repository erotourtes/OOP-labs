package com.github.erotourtes.view

import com.github.erotourtes.drawing.history.NewCommand
import com.github.erotourtes.drawing.history.OpenCommand
import com.github.erotourtes.drawing.shape.ShapeState
import com.github.erotourtes.utils.EditorInfo
import com.github.erotourtes.utils.g
import com.github.erotourtes.utils.n
import javafx.application.Platform
import javafx.embed.swing.SwingFXUtils
import javafx.scene.control.*
import javafx.scene.image.WritableImage
import javafx.stage.FileChooser
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import tornadofx.*
import java.awt.Desktop
import java.io.File
import javax.imageio.ImageIO
import kotlin.io.path.createTempFile

class MenuController : Controller() {
    private val model: CanvasModel by inject()

    fun new() {
        with(model) {
            val operation = NewCommand(sl).apply { execute() }
            h.add(operation)
        }
    }

    fun open() {
        chooseFile()?.let {
            with(model) {
                val shapes = shapeStatesFrom(it).map { s -> s.toShape(c.graphicsContext2D) }
                val operation = OpenCommand(sl, shapes).apply { execute() }
                h.add(operation)
            }
        }
    }

    fun saveAs() {
        saveFile()?.writeText(shapeStatesAsJSON)
    }

    fun print() {
        val c = model.c
        val image = WritableImage(c.width.toInt(), c.height.toInt())
        val tempPath = createTempFile(suffix = ".png")
        c.snapshot(null, image)
        ImageIO.write(
            SwingFXUtils.fromFXImage(image, null),
            tempPath.toString().substringAfter("."),
            tempPath.toFile()
        )

        Desktop.getDesktop().open(tempPath.toFile())
    }

    fun exit() = Platform.exit()

    fun create(): Menu {
        val group = ToggleGroup()
        val editorHandler = model.eh
        val list = model.ei

        editorHandler.listenToChanges { _, _, newValue ->
            group.toggles.forEach {
                val userData = it.userData as EditorInfo
                it.isSelected = userData.name == newValue
            }
        }

        val objectsUI = list.map {
            RadioMenuItem(it.name).apply {
                action { editorHandler.useEditor(it.name) }
                toggleGroup = group
                isSelected = false
                userData = it
            }
        }

        return Menu("Objects").apply { items.addAll(objectsUI) }
    }

    private fun saveFile(): File? {
        val fileChooser = FileChooser().apply {
            title = "Save as..."
            extensionFilters.addAll(
                FileChooser.ExtensionFilter("JSON", "*.json"),
                FileChooser.ExtensionFilter("ALL", "*.*"),
            )
        }

        return fileChooser.showSaveDialog(find<MainView>().currentWindow)
    }

    private fun chooseFile(): File? {
        val fileChooser = FileChooser().apply {
            title = "Choose file..."
            extensionFilters.addAll(
                FileChooser.ExtensionFilter("JSON", "*.json"),
                FileChooser.ExtensionFilter("ALL", "*.*"),
            )
        }

        return fileChooser.showOpenDialog(find<MainView>().currentWindow)
    }

    private fun shapeStatesFrom(it: File): List<ShapeState> {
        val json = Json { prettyPrint = true }
        val jsonShapeState = it.readText()
        return json.decodeFromString(ListSerializer(ShapeState.serializer), jsonShapeState)
    }

    private val shapeStatesAsJSON: String
        get() {
            val shapes = model.sl.getList().map { it.getState() }
            val json = Json { prettyPrint = true }

            return json.encodeToString(ListSerializer(ShapeState.serializer), shapes)
        }
}

class MyMenu : View() {
    private val ctrl: MenuController by inject(super.scope)

    override val root = menubar {
        menu("File") {
            item("New...") { action { ctrl.new() } }
            item("Open...") { action { ctrl.open() } }
            item("Save as...") { action { ctrl.saveAs() } }
            separator()
            item("Print") { action { ctrl.print() } }
            separator()
            item("Exit") { action { ctrl.exit() } }
        }

        menus.addAll(ctrl.create())

        menu("Help") {
            item(
                """
                    0) Ж = $g
                    1) Статичний масив (Ж mod 3 != 0) обсягом $g + 100 = $n.
                    2) "Гумовий" слід при вводі об’єктів - пунктирна лінія чорного кольору для (Ж mod 4 = 3) g % 4 = ${g % 4} 
                    3) Прямокутник:
                        Увід прямокутника:
                            - від центру до одного з кутів для (Ж mod 2 = 1) g % 2 = ${g % 2}
                        Відображення прямокутника:
                            - чорний контур прямокутника без заповнення для (Ж mod 5 = 3 або 4) g % 5 = ${g % 5}
                        Кольори заповнення прямокутника:
                            - сірий для (Ж mod 6 = 5) g % 6 = ${g % 6}
                    4) Еліпс:
                        Ввід еліпсу:
                            - по двом протилежним кутам охоплюючого прямокутника для варіантів (Ж mod 2 = 1) g % 2 = ${g % 2}
                        Відображення еліпсу:
                            - чорний контур з кольоровим заповненням для (Ж mod 5 = 3 або 4) g % 5 = ${g % 5} 
                        Кольори заповнення еліпсу:
                            - померанчевий для (Ж mod 6 = 5) g % 6 = ${g % 6}
                    5) Позначка поточного типу об’єкту, що вводиться
                            - в заголовку вікна для (Ж mod 2 = 1) g % 2 = ${g % 2}
                    """.trimIndent()
            )
        }
    }
}
