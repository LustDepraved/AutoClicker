package com.autoclicker.automation.detection

import android.graphics.Bitmap
import android.graphics.Rect
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject

class DetectionPipeline @Inject constructor(
    private val ocrManager: OCRManager,
    private val matcher: ImageMatcher,
    private val scanner: RegionScanner
) {
    suspend fun waitUntilText(bitmapProvider: suspend () -> Bitmap, text: String, timeoutMs: Long): Pair<Float, Float>? =
        withTimeoutOrNull(timeoutMs) {
            while (true) {
                val hit = ocrManager.findTextCenter(bitmapProvider(), text)
                if (hit != null) return@withTimeoutOrNull hit
                delay(250)
            }
            null
        }

    fun detectTemplate(bitmap: Bitmap, template: Bitmap): Pair<Float, Float>? = matcher.matchTemplate(bitmap, template)
    fun detectColor(bitmap: Bitmap, region: Rect, color: Int): Pair<Float, Float>? = scanner.findColor(bitmap, region, color)
}
