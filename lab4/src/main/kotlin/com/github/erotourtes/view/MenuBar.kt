package com.github.erotourtes.view

import com.github.erotourtes.drawing.EditorHandler
import com.github.erotourtes.utils.PopupView
import com.github.erotourtes.utils.EditorInfo
import com.github.erotourtes.utils.g
import com.github.erotourtes.utils.n
import javafx.scene.control.*
import javafx.scene.control.MenuBar
import javafx.stage.StageStyle
import tornadofx.*

class MenuBar(vararg menu: Menu) : MenuBar() {
    init {
        menu("File") {
            val invoke: MenuItem.() -> Unit = {
                action { find<PopupView>(PopupView.ScopeInfo(text)).openModal(StageStyle.UTILITY) }
            }

            item("New...") { invoke() }
            item("Open...") { invoke() }
            item("Save as...") { invoke() }
            separator()
            item("Print") { invoke() }
            separator()
            item("Exit") { invoke() }
        }

        menus.addAll(menu)

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

    companion object {
        fun create(editorHandler: EditorHandler, list: List<EditorInfo>): MenuBar {
            val group = ToggleGroup()

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

            val menu = Menu("Objects").apply { items.addAll(objectsUI) }

            return com.github.erotourtes.view.MenuBar(menu)
        }
    }
}
