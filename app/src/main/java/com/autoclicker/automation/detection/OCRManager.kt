package com.autoclicker.automation.detection

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class OCRManager @Inject constructor() {
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    suspend fun findTextCenter(bitmap: Bitmap, query: String): Pair<Float, Float>? {
        val result = recognizer.process(InputImage.fromBitmap(bitmap, 0)).await()
        val block = result.textBlocks.firstOrNull { it.text.contains(query, ignoreCase = true) } ?: return null
        val box = block.boundingBox ?: return null
        return box.exactCenterX() to box.exactCenterY()
    }
}
