package com.autoclicker.automation.script

import com.autoclicker.domain.model.ActionType
import org.junit.Assert.assertEquals
import org.junit.Test

class ScriptSerializerTest {
    @Test
    fun roundTrip() {
        val macros = MacroRegistry()
        val serializer = ScriptSerializer(ScenarioVersionManager(), macros)
        val deserializer = ScriptDeserializer(MigrationManager(ScenarioVersionManager()), macros)
        macros.putAll(mapOf("open_menu" to listOf(ScriptAction(ActionType.TAP, 0.5f, 0.5f, durationMs = 50, delayBeforeMs = 10))))
        val source = ScriptDocument(2, "scenario", "default", null, listOf(ScriptAction(ActionType.SWIPE, 0.1f, 0.2f, 0.8f, 0.9f, 300, 100)), macros.snapshot())
        val decoded = deserializer.deserialize(serializer.serialize(source))
        assertEquals("scenario", decoded.scenarioName)
        assertEquals(1, decoded.actions.size)
    }
}
