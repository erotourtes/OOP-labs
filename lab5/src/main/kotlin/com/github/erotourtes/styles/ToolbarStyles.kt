package com.github.erotourtes.styles

import tornadofx.*
import javafx.scene.paint.Color

class ToolbarStyles : Stylesheet() {

    companion object {
        val toolbar by cssclass()
        val iconButton by cssclass()
        val icon by cssclass()
        val dark = c("#555")
        val light = Color.LIGHTSTEELBLUE!!
    }

    init {
        val toolbarHeight = 40.px
        toolbar {
            padding = box(5.px)
            spacing = 5.px
            minHeight = toolbarHeight
            alignment = javafx.geometry.Pos.CENTER_LEFT
            borderWidth += box(0.px, 0.px, 1.px, 0.px)
            borderColor += box(dark)
        }

        iconButton {
            backgroundColor += Color.TRANSPARENT

            and(selected) {
                backgroundColor += dark
                icon { fill = light }
            }

            minHeight = toolbarHeight / 1.5
            maxHeight = toolbarHeight / 1.5
        }

        icon { fill = dark }
    }
}