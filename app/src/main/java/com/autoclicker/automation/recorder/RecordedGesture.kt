package com.autoclicker.automation.recorder

data class RecordedPoint(val x: Float, val y: Float, val t: Long)

data class RecordedGesture(
    val downTimeOffsetMs: Long,
    val upTimeOffsetMs: Long,
    val points: List<RecordedPoint>,
    val pressureApprox: Float,
    val playbackSpeed: Float = 1f
)
