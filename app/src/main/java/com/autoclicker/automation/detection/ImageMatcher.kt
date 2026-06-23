package com.autoclicker.automation.detection

import android.graphics.Bitmap
import javax.inject.Inject

class ImageMatcher @Inject constructor() {
    fun matchTemplate(source: Bitmap, template: Bitmap, threshold: Float = 0.92f): Pair<Float, Float>? {
        if (template.width > source.width || template.height > source.height) return null
        var bestScore = -1f
        var bestX = 0
        var bestY = 0
        val step = 4
        for (y in 0 until (source.height - template.height) step step) {
            for (x in 0 until (source.width - template.width) step step) {
                var same = 0
                var total = 0
                for (ty in 0 until template.height step 8) {
                    for (tx in 0 until template.width step 8) {
                        total++
                        if (source.getPixel(x + tx, y + ty) == template.getPixel(tx, ty)) same++
                    }
                }
                val score = same.toFloat() / total
                if (score > bestScore) { bestScore = score; bestX = x; bestY = y }
            }
        }
        return if (bestScore >= threshold) bestX + template.width / 2f to bestY + template.height / 2f else null
    }
}
