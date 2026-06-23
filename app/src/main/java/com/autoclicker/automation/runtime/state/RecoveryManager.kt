package com.autoclicker.automation.runtime.state

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecoveryManager @Inject constructor(
    private val store: RuntimeStateStore
) {
    fun recover(): RuntimeSnapshot = store.readSnapshot()
}
