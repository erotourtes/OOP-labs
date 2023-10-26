package com.github.erotourtes.drawing.editor

import com.github.erotourtes.utils.Dimension

fun interface DmProcessor {
    fun process(dm: Dimension): Dimension
}