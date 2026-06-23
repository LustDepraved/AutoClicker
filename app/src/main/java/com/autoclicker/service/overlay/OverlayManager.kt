package com.autoclicker.service.overlay

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.provider.Settings
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Button
import com.autoclicker.R
import com.autoclicker.automation.runtime.AutomationRuntime
import com.autoclicker.core.logging.AppLogger
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OverlayManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val runtime: AutomationRuntime,
    private val logger: AppLogger
) {
    private val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private var overlayView: View? = null

    fun canDraw(): Boolean = Settings.canDrawOverlays(context)

    fun show(): Boolean {
        if (!canDraw()) {
            logger.e(TAG, "Overlay permission missing")
            return false
        }
        if (overlayView != null) return true
        return try {
            val view = LayoutInflater.from(context).inflate(R.layout.overlay_controls, null)
            val params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            ).apply {
                gravity = Gravity.TOP or Gravity.START
                x = 20
                y = 220
            }
            setupDrag(view, params)
            view.findViewById<Button>(R.id.btnStart).setOnClickListener { runtime.start() }
            view.findViewById<Button>(R.id.btnStop).setOnClickListener { runtime.stop() }
            view.findViewById<Button>(R.id.btnPause).setOnClickListener { runtime.pause() }
            view.findViewById<Button>(R.id.btnResume).setOnClickListener { runtime.resume() }
            wm.addView(view, params)
            overlayView = view
            true
        } catch (t: Throwable) {
            logger.e(TAG, "Failed showing overlay", t)
            false
        }
    }

    fun hide() {
        overlayView?.let {
            runCatching { wm.removeView(it) }
            overlayView = null
        }
    }

    private fun setupDrag(view: View, params: WindowManager.LayoutParams) {
        view.setOnTouchListener(object : View.OnTouchListener {
            private var initialX = 0
            private var initialY = 0
            private var touchX = 0f
            private var touchY = 0f
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> { initialX = params.x; initialY = params.y; touchX = event.rawX; touchY = event.rawY; return true }
                    MotionEvent.ACTION_MOVE -> {
                        params.x = initialX + (event.rawX - touchX).toInt()
                        params.y = initialY + (event.rawY - touchY).toInt()
                        runCatching { wm.updateViewLayout(view, params) }
                        return true
                    }
                }
                return false
            }
        })
    }

    companion object { private const val TAG = "OverlayManager" }
}
