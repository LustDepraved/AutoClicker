package com.autoclicker.automation.script

import com.autoclicker.domain.model.ActionType
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

class ScriptSerializer @Inject constructor(
    private val versionManager: ScenarioVersionManager,
    private val macroRegistry: MacroRegistry
) {
    fun serialize(document: ScriptDocument): String {
        val payload = JSONObject()
            .put("schemaVersion", versionManager.currentVersion)
            .put("scenarioName", document.scenarioName)
            .put("profileName", document.profileName)
            .put("targetPackage", document.targetPackage)
            .put("actions", actionsArray(document.actions))
            .put("macros", macrosObject(macroRegistry.snapshot()))

        val digest = payload.toString().hashCode().toString()
        return payload.put("integrity", digest).toString(2)
    }

    private fun actionsArray(actions: List<ScriptAction>) = JSONArray().apply {
        actions.forEach { put(actionObject(it)) }
    }

    private fun actionObject(action: ScriptAction) = JSONObject()
        .put("type", action.type.name)
        .put("x", action.x)
        .put("y", action.y)
        .put("x2", action.x2)
        .put("y2", action.y2)
        .put("durationMs", action.durationMs)
        .put("delayBeforeMs", action.delayBeforeMs)
        .put("macroRef", action.macroRef)
        .put("nested", actionsArray(action.nested))

    private fun macrosObject(macros: Map<String, List<ScriptAction>>) = JSONObject().apply {
        macros.forEach { (name, actions) -> put(name, actionsArray(actions)) }
    }
}
