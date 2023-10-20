package com.github.erotourtes.view

import com.github.erotourtes.utils.MenuItemInfo
import javafx.scene.control.MenuBar
import tornadofx.*

const val g = 22
const val n = 100 + g

class MenuBar(shapes : List<MenuItemInfo>) : MenuBar() {
    init {
        menu("Файл") {
            item("New")
            item("Open")
            item("Save")
            item("Save as")
            separator()
            item("Exit")
        }
        menu("Об'єкти") {
            for (shape in shapes) {
                radiomenuitem(shape.name, shape.group) {
                    action(shape.action)
                    isSelected = shape.selected
                }
            }
        }
        menu("Довідка") {
            item(
                """
                    0) Ж = $g
                    1) Статичний масив (Ж mod 3 != 0) обсягом $g + 100 = $n.
                    2) "Гумовий" слід при вводі об’єктів - суцільна лінія синього кольору для варіантів (Ж mod 4 = 2)
                    3) Позначка поточного типу об’єкту, що вводиться - в меню (метод OnInitMenuPopup) для варіантів (Ж mod 2 = 0)
                    4) прямокутник:
                        - по двом протилежним кутам для варіантів (Ж mod 2 = 0)
                        - чорний контур з кольоровим заповненням для (Ж mod 5 = 1 або 2)
                        - померанчевий для (Ж mod 6 = 4)
                    5) еліпс
                        - від центру до одного з кутів охоплюючого прямокутника для варіантів (Ж mod 2 = 0)
                        - чорний контур еліпсу без заповнення для (Ж mod 5 = 0 або 2)
                        - рожевий для (Ж mod 6 = 4)
                    """.trimIndent()
            )
        }
    }
}
