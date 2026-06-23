package com.autoclicker.automation.runtime.state

import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SchedulerEngine @Inject constructor() {
    suspend fun waitFor(startAfterMs: Long) {
        if (startAfterMs > 0) delay(startAfterMs)
    }
}
