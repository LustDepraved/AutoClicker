package com.autoclicker.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.autoclicker.databinding.ActivityMainBinding
import com.autoclicker.service.foreground.AutomationForegroundService
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.seedScenario()
        setupUi()
        updateStatus()
    }

    override fun onResume() {
        super.onResume()
        updateStatus()
    }

    private fun setupUi() {
        binding.openAccessibility.setOnClickListener { startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)) }
        binding.openOverlayPermission.setOnClickListener {
            startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName")))
        }
        binding.startRuntime.setOnClickListener {
            if (!Settings.canDrawOverlays(this)) {
                updateStatus("Overlay permission required")
                return@setOnClickListener
            }
            val intent = Intent(this, AutomationForegroundService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) startForegroundService(intent) else startService(intent)
            updateStatus("Runtime started")
        }
        binding.stopRuntime.setOnClickListener {
            stopService(Intent(this, AutomationForegroundService::class.java))
            updateStatus("Runtime stopped")
        }
    }

    private fun updateStatus(extra: String = "") {
        val overlay = Settings.canDrawOverlays(this)
        binding.statusText.text = "Overlay: $overlay. $extra Enable Accessibility Service manually."
    }
}
