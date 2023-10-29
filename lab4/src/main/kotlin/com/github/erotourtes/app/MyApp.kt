package com.github.erotourtes.app

import com.github.erotourtes.styles.ToolbarStyles
import com.github.erotourtes.view.MainView
import tornadofx.App

class MyApp: App(MainView::class, ToolbarStyles::class)