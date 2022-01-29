package ru.aasmc.wordify.common.core.utils

import kotlin.math.abs
import kotlin.math.max

/**
 * Function to compare floating point numbers.
 */
fun Double.equalsDelta(other: Double) =
    abs(this - other) < max(Math.ulp(this), Math.ulp(other)) * 2

fun Float.equalsDelta(other: Float) =
    abs(this - other) < max(Math.ulp(this), Math.ulp(other)) * 2