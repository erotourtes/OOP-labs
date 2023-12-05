package com.github.erotourtes.main

import com.github.erotourtes.main.view.MainController
import com.github.erotourtes.main.view.MainView
import com.github.erotourtes.utils.Logger
import tornadofx.*
import java.lang.management.ManagementFactory

class MainApp : App(MainView::class) {
    override fun init() {
        super.init()
        val pid = ManagementFactory.getRuntimeMXBean().name
        Logger.preMessage = "MainApp($pid)"
    }

    override fun stop() {
        Logger.log("stop method")
        find<MainController>().dispose()
        Logger.log("stop method end", Logger.InfoType.WARNING)
    }
}