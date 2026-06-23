package com.autoclicker.automation.script

import com.autoclicker.domain.model.ActionType
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

class ScriptDeserializer @Inject constructor(
    private val migrationManager: MigrationManager,
    private val macroRegistry: MacroRegistry
) {
    fun deserialize(raw: String): ScriptDocument {
        val json = JSONObject(raw)
        val storedIntegrity = json.optString("integrity")
        json.remove("integrity")
        val calculated = json.toString().hashCode().toString()
        require(storedIntegrity == calculated) { "Script integrity check failed" }

        val doc = ScriptDocument(
            schemaVersion = json.getInt("schemaVersion"),
            scenarioName = json.getString("scenarioName"),
            profileName = json.optString("profileName", "default"),
            targetPackage = json.optString("targetPackage").ifBlank { null },
            actions = parseActions(json.getJSONArray("actions")),
            macros = parseMacros(json.optJSONObject("macros") ?: JSONObject())
        )
        macroRegistry.putAll(doc.macros)
        return migrationManager.migrate(doc)
    }

    private fun parseMacros(obj: JSONObject): Map<String, List<ScriptAction>> = buildMap {
        obj.keys().forEach { key -> put(key, parseActions(obj.getJSONArray(key))) }
    }

    private fun parseActions(array: JSONArray): List<ScriptAction> = buildList {
        for (i in 0 until array.length()) {
            val it = array.getJSONObject(i)
            add(
                ScriptAction(
                    type = ActionType.valueOf(it.getString("type")),
                    x = it.getDouble("x").toFloat(),
                    y = it.getDouble("y").toFloat(),
                    x2 = it.optDouble("x2", Double.NaN).takeUnless { d -> d.isNaN() }?.toFloat(),
                    y2 = it.optDouble("y2", Double.NaN).takeUnless { d -> d.isNaN() }?.toFloat(),
                    durationMs = it.getLong("durationMs"),
                    delayBeforeMs = it.getLong("delayBeforeMs"),
                    macroRef = it.optString("macroRef").ifBlank { null },
                    nested = parseActions(it.optJSONArray("nested") ?: JSONArray())
                )
            )
        }
    }
}
