package com.autoclicker.automation.engine

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.graphics.PointF
import com.autoclicker.domain.model.ActionStep
import com.autoclicker.domain.model.ActionType
import com.autoclicker.service.accessibility.AccessibilityServiceHolder
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class GestureEngine @Inject constructor(
    private val holder: AccessibilityServiceHolder,
    private val mapper: CoordinateMapper,
    private val humanizer: HumanizerEngine
) {
    suspend fun perform(step: ActionStep): Boolean {
        val service = holder.get() ?: return false
        val p1 = mapper.toScreen(step.normalizedX, step.normalizedY)
        val start = humanizer.jitter(PointF(p1.x.toFloat(), p1.y.toFloat()))
        val builder = GestureDescription.Builder()
        when (step.type) {
            ActionType.TAP, ActionType.LONG_TAP -> {
                val path = Path().apply { moveTo(start.x, start.y) }
                builder.addStroke(GestureDescription.StrokeDescription(path, 0, humanizer.humanDuration(step.durationMs.coerceAtLeast(1))))
            }
            ActionType.SWIPE, ActionType.DRAG -> {
                val p2 = mapper.toScreen(step.normalizedX2 ?: step.normalizedX, step.normalizedY2 ?: step.normalizedY)
                val end = humanizer.jitter(PointF(p2.x.toFloat(), p2.y.toFloat()))
                val (s, c1, e) = humanizer.buildCurvePoints(start, end)
                val path = Path().apply {
                    moveTo(s.x, s.y)
                    quadTo(c1.x, c1.y, e.x, e.y)
                }
                builder.addStroke(GestureDescription.StrokeDescription(path, 0, humanizer.humanDuration(step.durationMs)))
            }
            ActionType.PINCH, ActionType.ZOOM -> {
                val p2 = mapper.toScreen(step.normalizedX2 ?: step.normalizedX, step.normalizedY2 ?: step.normalizedY)
                val center = PointF(((p1.x + p2.x) / 2f), ((p1.y + p2.y) / 2f))
                val offset = 80f
                val aStart = if (step.type == ActionType.PINCH) PointF(center.x - offset, center.y) else PointF(center.x, center.y)
                val aEnd = if (step.type == ActionType.PINCH) PointF(center.x, center.y) else PointF(center.x - offset, center.y)
                val bStart = if (step.type == ActionType.PINCH) PointF(center.x + offset, center.y) else PointF(center.x, center.y)
                val bEnd = if (step.type == ActionType.PINCH) PointF(center.x, center.y) else PointF(center.x + offset, center.y)
                builder.addStroke(stroke(aStart, aEnd, step.durationMs))
                builder.addStroke(stroke(bStart, bEnd, step.durationMs))
            }
        }
        return dispatch(service, builder.build())
    }

    private fun stroke(from: PointF, to: PointF, durationMs: Long): GestureDescription.StrokeDescription {
        val path = Path().apply { moveTo(from.x, from.y); quadTo((from.x + to.x) / 2f, (from.y + to.y) / 2f, to.x, to.y) }
        return GestureDescription.StrokeDescription(path, 0, humanizer.humanDuration(durationMs))
    }

    private suspend fun dispatch(service: AccessibilityService, gesture: GestureDescription): Boolean = suspendCancellableCoroutine { cont ->
        val result = service.dispatchGesture(gesture, object : AccessibilityService.GestureResultCallback() {
            override fun onCompleted(gestureDescription: GestureDescription?) { if (cont.isActive) cont.resume(true) }
            override fun onCancelled(gestureDescription: GestureDescription?) { if (cont.isActive) cont.resume(false) }
        }, null)
        if (!result && cont.isActive) cont.resume(false)
    }
}
