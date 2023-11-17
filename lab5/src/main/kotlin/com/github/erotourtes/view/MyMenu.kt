package com.github.erotourtes.view

import com.github.erotourtes.drawing.history.NewCommand
import com.github.erotourtes.drawing.history.OpenCommand
import com.github.erotourtes.drawing.shape.ShapeState
import com.github.erotourtes.utils.EditorInfo
import com.github.erotourtes.utils.g
import com.github.erotourtes.utils.n
import com.github.erotourtes.utils.shapeStatesToJSON
import javafx.application.Platform
import javafx.embed.swing.SwingFXUtils
import javafx.scene.control.*
import javafx.stage.FileChooser
import javafx.stage.Modality
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import tornadofx.*
import java.awt.Desktop
import java.io.File
import javax.imageio.ImageIO
import kotlin.io.path.createTempFile

class MenuController : Controller() {
    private val model by inject<CanvasModel>()
    private val editorsInfoModel by inject<EditorsInfoModel>()

    fun new() {
        with(model) {
            val operation = NewCommand(sl).apply { execute() }
            h.add(operation)
        }
    }

    fun open() {
        chooseFile()?.let {
            with(model) {
                val shapes = shapeStatesFrom(it).map { it.toShape() }
                OpenCommand(sl, shapes).apply { execute() }.let(h::add)
            }
        }
    }

    fun saveAs() {
        saveFile()?.writeText(shapeStatesToJSON(model.sl.getStatesList()))
    }

    fun print() {
        val tempPath = createTempFile(suffix = ".png")
        val image = model.cc.getSnapshotImage()
        ImageIO.write(
            SwingFXUtils.fromFXImage(image, null),
            tempPath.toString().substringAfter("."),
            tempPath.toFile()
        )

        runAsync {
            try {
                val desktop = if (Desktop.isDesktopSupported()) Desktop.getDesktop() else null
                desktop?.open(tempPath.toFile())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun exit() = Platform.exit()

    fun create(): Menu {
        val group = ToggleGroup()
        val editorHandler = model.eh
        val list = editorsInfoModel.editorsInfo.value

        editorHandler.listenToChanges { _, _, _ ->
            group.toggles.forEach {
                val userData = it.userData as EditorInfo
                it.isSelected = editorHandler.isCurShapeActive(userData.pair)
            }
        }

        val objectsUI = list.map {
            RadioMenuItem(it.name).apply {
                action { editorHandler.use(it.pair) }
                toggleGroup = group
                isSelected = false
                userData = it
            }
        }

        return Menu("Objects").apply { items.addAll(objectsUI) }
    }

    fun openTable() {
        find<TableView>().openModal(
            modality = Modality.NONE,
            escapeClosesWindow = true,
            owner = find<MainView>().currentWindow
        )
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
}

class MyMenu : View() {
    private val ctrl by inject<MenuController>()

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

        menu("Table") { item("Show") { action(ctrl::openTable) } }

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
