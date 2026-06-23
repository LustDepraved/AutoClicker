package com.autoclicker.automation.script

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MacroRegistry @Inject constructor() {
    private val macros = linkedMapOf<String, List<ScriptAction>>()

    fun putAll(data: Map<String, List<ScriptAction>>) {
        macros.clear()
        macros.putAll(data)
    }

    fun resolve(name: String): List<ScriptAction> = macros[name].orEmpty()

    fun snapshot(): Map<String, List<ScriptAction>> = macros.toMap()
}
