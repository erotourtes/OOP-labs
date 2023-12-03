package com.github.erotourtes.first

import tornadofx.*

class FirstApp : App(MainView::class)


class MainView : View("Hello TornadoFX") {
    override val root = vbox {
        label("Hello TornadoFX")
    }
}