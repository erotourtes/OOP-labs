package com.github.erotourtes.view

import com.github.erotourtes.utils.MenuItemInfo
import javafx.scene.control.MenuBar
import tornadofx.*

const val g = 22 + 1
const val n = 100 + g

class MenuBar(shapes: List<MenuItemInfo>) : MenuBar() {
    init {
        menu("File") {
            item("New...")
            item("Open...")
            item("Save as...")
            separator()
            item("Print")
            separator()
            item("Exit")
        }
        menu("Objects") {
            for (shape in shapes) {
                radiomenuitem(shape.name, shape.group) {
                    action(shape.action)
                    isSelected = shape.selected
                }
            }
        }
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
