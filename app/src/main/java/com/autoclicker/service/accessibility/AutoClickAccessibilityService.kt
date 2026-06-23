package com.autoclicker.service.accessibility

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import com.autoclicker.core.logging.AppLogger
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AutoClickAccessibilityService : AccessibilityService() {

    @Inject lateinit var holder: AccessibilityServiceHolder
    @Inject lateinit var logger: AppLogger

    override fun onServiceConnected() {
        super.onServiceConnected()
        holder.set(this)
        logger.i(TAG, "Accessibility connected")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) = Unit

    override fun onInterrupt() {
        logger.d(TAG, "Accessibility interrupted")
    }

    override fun onDestroy() {
        holder.set(null)
        super.onDestroy()
    }

    companion object { private const val TAG = "AutoClickAccessibility" }
}
