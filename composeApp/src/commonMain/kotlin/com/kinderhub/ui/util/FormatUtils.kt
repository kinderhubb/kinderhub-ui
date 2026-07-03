package com.kinderhub.ui.util

import kotlin.math.roundToInt

/**
 * Formats a Double as a price string with 2 decimal places.
 * Platform-agnostic alternative to String.format("%.2f", value).
 */
fun formatPrice(value: Double): String {
    val rounded = (value * 100).roundToInt() / 100.0
    val intPart = rounded.toLong()
    val decPart = ((rounded - intPart) * 100).roundToInt()
    return "$intPart.${decPart.toString().padStart(2, '0')}"
}

/**
 * Formats a rating to 1 decimal place (e.g., 4.9).
 * Shows integer if decimal is 0 (e.g., 5.0 → 5).
 */
fun formatRating(value: Float): String = formatOneDecimal(value.toDouble())
fun formatRating(value: Double): String = formatOneDecimal(value)

/**
 * Formats a distance to 1 decimal place (e.g., 0.6 mi).
 * Shows integer if decimal is 0 (e.g., 2.0 → 2).
 */
fun formatDistance(value: Float): String = formatOneDecimal(value.toDouble())
fun formatDistance(value: Double): String = formatOneDecimal(value)

/**
 * Internal helper to format a number to 1 decimal place.
 */
private fun formatOneDecimal(value: Double): String {
    val rounded = (value * 10).roundToInt() / 10.0
    return if (rounded == rounded.toLong().toDouble()) {
        rounded.toLong().toString()
    } else {
        val intPart = rounded.toLong()
        val decPart = ((rounded - intPart) * 10).roundToInt()
        "$intPart.$decPart"
    }
}
