package com.autoclicker.automation.detection

import android.graphics.Bitmap
import android.graphics.Rect
import javax.inject.Inject

class RegionScanner @Inject constructor() {
    fun findColor(bitmap: Bitmap, region: Rect, color: Int, tolerance: Int = 8): Pair<Float, Float>? {
        val left = region.left.coerceIn(0, bitmap.width - 1)
        val top = region.top.coerceIn(0, bitmap.height - 1)
        val right = region.right.coerceIn(1, bitmap.width)
        val bottom = region.bottom.coerceIn(1, bitmap.height)
        for (y in top until bottom step 2) {
            for (x in left until right step 2) {
                if (close(bitmap.getPixel(x, y), color, tolerance)) return x.toFloat() to y.toFloat()
            }
        }
        return null
    }

    private fun close(a: Int, b: Int, t: Int): Boolean {
        val ar = (a shr 16) and 0xFF
        val ag = (a shr 8) and 0xFF
        val ab = a and 0xFF
        val br = (b shr 16) and 0xFF
        val bg = (b shr 8) and 0xFF
        val bb = b and 0xFF
        return kotlin.math.abs(ar - br) <= t && kotlin.math.abs(ag - bg) <= t && kotlin.math.abs(ab - bb) <= t
    }
}
