package com.github.erotourtes.data

import tornadofx.*
import java.lang.IllegalStateException

class MainModel : ItemViewModel<MainState>() {
    val nProp = bind(MainState::n)
    val minProp = bind(MainState::minValue)
    val maxProp = bind(MainState::maxValue)

    override fun onCommit() {
        super.onCommit()
        if (min > max) throw IllegalStateException("min > max")
        if (n < 0) throw IllegalStateException("n < 0")
    }

    val min by minProp
    val max by maxProp
    val n by nProp
}

data class MainState(var n: Int = 0, var minValue: Double = 0.0, var maxValue: Double = 0.0) {
    override fun toString(): String {
        return "$n $minValue $maxValue"
    }

    companion object {
        fun fromString(string: String): MainState? {
            return runCatching {
                val values = string.split(" ")
                MainState(values[0].toInt(), values[1].toDouble(), values[2].toDouble())
            }.getOrNull()
        }
    }
}