package com.autoclicker.automation.script

import com.autoclicker.domain.model.ActionType

data class ScriptDocument(
    val schemaVersion: Int,
    val scenarioName: String,
    val profileName: String,
    val targetPackage: String?,
    val actions: List<ScriptAction>,
    val macros: Map<String, List<ScriptAction>>
)

data class ScriptAction(
    val type: ActionType,
    val x: Float,
    val y: Float,
    val x2: Float? = null,
    val y2: Float? = null,
    val durationMs: Long,
    val delayBeforeMs: Long,
    val macroRef: String? = null,
    val nested: List<ScriptAction> = emptyList()
)
