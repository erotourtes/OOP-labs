package com.github.erotourtes.data

import tornadofx.*
import kotlin.math.abs

class MainModel : ItemViewModel<MainState>() {
    val nValueProp = bind(MainState::n)
    val minValueProp = bind(MainState::minValue)
    val maxValueProp = bind(MainState::maxValue)

    override fun onCommit() {
        nValueProp.value = abs(n)
        if (min > max)
            minValueProp.value = max.also { maxValueProp.value = min }

        super.onCommit()
    }

    val min by minValueProp
    val max by maxValueProp
    val n by nValueProp
}

data class MainState(var n: Int = 0, var minValue: Double = 0.0, var maxValue: Double = 0.0) {
    override fun toString(): String {
        return "$n $minValue $maxValue"
    }

    companion object {
        fun fromString(string: String): MainState? {
            return try {
                val values = string.split(" ")
                MainState(values[0].toInt(), values[1].toDouble(), values[2].toDouble())
            } catch (e: Exception) {
                null
            }
        }
    }
}