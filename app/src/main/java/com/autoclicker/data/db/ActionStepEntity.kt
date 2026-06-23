package com.autoclicker.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "action_steps")
data class ActionStepEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val orderInScenario: Int,
    val type: String,
    val normalizedX: Float,
    val normalizedY: Float,
    val normalizedX2: Float? = null,
    val normalizedY2: Float? = null,
    val durationMs: Long,
    val delayBeforeMs: Long,
    val pressureApprox: Float = 0.5f,
    val profile: String? = null
)
