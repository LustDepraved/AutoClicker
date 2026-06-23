package com.autoclicker.automation.script

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MigrationManager @Inject constructor(
    private val versionManager: ScenarioVersionManager
) {
    fun migrate(document: ScriptDocument): ScriptDocument {
        if (document.schemaVersion >= versionManager.currentVersion) return document
        return document.copy(schemaVersion = versionManager.currentVersion)
    }
}
