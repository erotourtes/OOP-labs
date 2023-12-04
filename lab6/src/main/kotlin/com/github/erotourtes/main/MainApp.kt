package com.github.erotourtes.main

import com.github.erotourtes.main.view.MainController
import com.github.erotourtes.main.view.MainView
import com.github.erotourtes.utils.logger
import tornadofx.*

class MainApp : App(MainView::class) {
    override fun stop() {
        logger("MainApp(stop)")
        find<MainController>().dispose()
    }
}