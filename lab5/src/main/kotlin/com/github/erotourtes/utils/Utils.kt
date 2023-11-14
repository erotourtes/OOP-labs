package com.github.erotourtes.utils

import com.github.erotourtes.drawing.editor.DmProcessor
import com.github.erotourtes.drawing.editor.Editor
import com.github.erotourtes.drawing.shape.Shape
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon

data class EditorInfo(
    val name: String,
    val tooltip: String,
    val pair: Pair<Class<out Shape>, Editor>,
    // [icons](https://fontawesome.com/v4/icons/)
    var icon: FontAwesomeIcon? = null,
)

const val g = 22 + 1
const val n = 100 + g

fun pipe(vararg processors: DmProcessor): DmProcessor = DmProcessor { dm ->
    processors.fold(dm) { acc, processor -> processor.process(acc) }
}

open class SingletonHolder<out T>(private val constructor: () -> T) {

    @Volatile
    private var instance: T? = null

    fun getInstance(): T =
        instance ?: synchronized(this) {
            instance ?: constructor().also { instance = it }
        }
}