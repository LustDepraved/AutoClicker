package com.autoclicker.core.logging

import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppLogger @Inject constructor() {
    fun d(tag: String, message: String) = Log.d(tag, message)
    fun i(tag: String, message: String) = Log.i(tag, message)
    fun e(tag: String, message: String, throwable: Throwable? = null) = Log.e(tag, message, throwable)
}
