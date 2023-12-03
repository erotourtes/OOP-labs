package com.github.erotourtes.main

import com.github.erotourtes.main.view.MainController
import com.github.erotourtes.main.view.MainView
import tornadofx.*

class MainApp : App(MainView::class) {
    override fun stop() {
        find<MainController>().dispose()

        super.stop()
    }
}