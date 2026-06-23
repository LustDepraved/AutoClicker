package com.autoclicker.service.accessibility

import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccessibilityServiceHolder @Inject constructor() {
    private val ref = AtomicReference<AutoClickAccessibilityService?>(null)

    fun set(service: AutoClickAccessibilityService?) {
        ref.set(service)
    }

    fun get(): AutoClickAccessibilityService? = ref.get()
}
