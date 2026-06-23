package com.autoclicker.domain.model

data class ActionStep(
    val id: Long,
    val orderInScenario: Int,
    val type: ActionType,
    val normalizedX: Float,
    val normalizedY: Float,
    val normalizedX2: Float? = null,
    val normalizedY2: Float? = null,
    val durationMs: Long,
    val delayBeforeMs: Long,
    val pressureApprox: Float = 0.5f,
    val profile: String? = null
)
