package com.autoclicker.automation.runtime.state

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RuntimeStateStore @Inject constructor(
    private val prefs: SharedPreferences
) {
    fun saveSnapshot(running: Boolean, paused: Boolean, stepIndex: Int) {
        prefs.edit().putBoolean("running", running).putBoolean("paused", paused).putInt("step", stepIndex).apply()
    }

    fun readSnapshot(): RuntimeSnapshot = RuntimeSnapshot(
        running = prefs.getBoolean("running", false),
        paused = prefs.getBoolean("paused", false),
        stepIndex = prefs.getInt("step", 0)
    )
}

data class RuntimeSnapshot(val running: Boolean, val paused: Boolean, val stepIndex: Int)
