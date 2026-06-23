package com.autoclicker.automation.engine

import android.graphics.PointF
import kotlin.random.Random
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HumanizerEngine @Inject constructor() {
    fun jitter(point: PointF, px: Float = 6f): PointF = PointF(
        point.x + Random.nextFloat() * px - (px / 2f),
        point.y + Random.nextFloat() * px - (px / 2f)
    )

    fun humanDuration(base: Long): Long {
        val factor = 0.85f + Random.nextFloat() * 0.35f
        return (base * factor).toLong().coerceAtLeast(16)
    }

    fun buildCurvePoints(start: PointF, end: PointF): Triple<PointF, PointF, PointF> {
        val midX = (start.x + end.x) / 2f
        val midY = (start.y + end.y) / 2f
        val c1 = PointF(midX + Random.nextFloat() * 60f - 30f, midY + Random.nextFloat() * 90f - 45f)
        val c2 = PointF(midX + Random.nextFloat() * 60f - 30f, midY + Random.nextFloat() * 90f - 45f)
        return Triple(jitter(start), c1, jitter(end))
    }
}
