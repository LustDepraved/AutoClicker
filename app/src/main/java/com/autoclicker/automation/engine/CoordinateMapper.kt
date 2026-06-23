package com.autoclicker.automation.engine

import android.content.Context
import android.graphics.Point
import android.view.WindowManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.roundToInt

@Singleton
class CoordinateMapper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun toScreen(normalizedX: Float, normalizedY: Float): Point {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val bounds = wm.currentWindowMetrics.bounds
        val x = (bounds.width() * normalizedX.coerceIn(0f, 1f)).roundToInt()
        val y = (bounds.height() * normalizedY.coerceIn(0f, 1f)).roundToInt()
        return Point(x, y)
    }
}
