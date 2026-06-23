package com.autoclicker.automation.recorder

import android.os.SystemClock
import android.view.MotionEvent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecorderEngine @Inject constructor() {
    private var startedAt = 0L
    private var recording = false
    private var downAt = 0L
    private var points = mutableListOf<RecordedPoint>()
    private val gestures = mutableListOf<RecordedGesture>()

    fun startRecording() {
        gestures.clear()
        points.clear()
        recording = true
        startedAt = SystemClock.elapsedRealtime()
    }

    fun stopRecording(playbackSpeed: Float = 1f): List<RecordedGesture> {
        recording = false
        return gestures.map { it.copy(playbackSpeed = playbackSpeed) }
    }

    fun onMotionEvent(event: MotionEvent, fromOverlay: Boolean = false) {
        if (!recording || fromOverlay) return
        val now = SystemClock.elapsedRealtime()
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                downAt = now
                points = mutableListOf(RecordedPoint(event.rawX, event.rawY, now - startedAt))
            }
            MotionEvent.ACTION_MOVE -> points += RecordedPoint(event.rawX, event.rawY, now - startedAt)
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                points += RecordedPoint(event.rawX, event.rawY, now - startedAt)
                gestures += RecordedGesture(downAt - startedAt, now - startedAt, compress(points), event.pressure)
            }
        }
    }

    fun trim(fromIndex: Int, toIndex: Int) { replace(gestures.subList(fromIndex.coerceAtLeast(0), toIndex.coerceAtMost(gestures.size)).toList()) }
    fun split(index: Int): Pair<RecordedGesture, RecordedGesture>? {
        val g = gestures.getOrNull(index) ?: return null
        val mid = g.points.size / 2
        val a = g.copy(points = g.points.take(mid))
        val b = g.copy(points = g.points.drop(mid))
        return a to b
    }
    fun reorder(from: Int, to: Int) { if (from in gestures.indices && to in gestures.indices) gestures.add(to, gestures.removeAt(from)) }
    fun duplicate(index: Int) { gestures.getOrNull(index)?.let { gestures.add(index + 1, it.copy()) } }
    fun delete(index: Int) { if (index in gestures.indices) gestures.removeAt(index) }
    fun merge(first: Int, second: Int) {
        val a = gestures.getOrNull(first) ?: return
        val b = gestures.getOrNull(second) ?: return
        gestures[first] = a.copy(upTimeOffsetMs = b.upTimeOffsetMs, points = a.points + b.points)
        gestures.removeAt(second)
    }

    private fun replace(list: List<RecordedGesture>) { gestures.clear(); gestures.addAll(list) }
    private fun compress(input: List<RecordedPoint>): List<RecordedPoint> = input.filterIndexed { i, _ -> i % 2 == 0 || i == input.lastIndex }
}
