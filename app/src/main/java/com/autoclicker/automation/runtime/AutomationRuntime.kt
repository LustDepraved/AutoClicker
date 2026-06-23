package com.autoclicker.automation.runtime

import com.autoclicker.automation.engine.GestureEngine
import com.autoclicker.automation.runtime.state.ExecutionQueue
import com.autoclicker.automation.runtime.state.RecoveryManager
import com.autoclicker.automation.runtime.state.RuntimeStateStore
import com.autoclicker.automation.runtime.state.SchedulerEngine
import com.autoclicker.core.logging.AppLogger
import com.autoclicker.domain.usecase.GetScenarioUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AutomationRuntime @Inject constructor(
    private val getScenarioUseCase: GetScenarioUseCase,
    private val gestureEngine: GestureEngine,
    private val schedulerEngine: SchedulerEngine,
    private val queue: ExecutionQueue,
    private val stateStore: RuntimeStateStore,
    private val recoveryManager: RecoveryManager,
    private val logger: AppLogger
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var runningJob: Job? = null
    @Volatile private var paused = false
    @Volatile private var emergencyStopped = false

    fun recoverIfNeeded() {
        val snap = recoveryManager.recover()
        paused = snap.paused
        if (snap.running) start(loop = true, startAfterMs = 500)
    }

    @Synchronized
    fun start(loop: Boolean = false, startAfterMs: Long = 0) {
        if (runningJob?.isActive == true) return
        emergencyStopped = false
        runningJob = scope.launch {
            schedulerEngine.waitFor(startAfterMs)
            do {
                val profile = queue.dequeue() ?: "default"
                logger.d(TAG, "Running profile=$profile")
                val steps = getScenarioUseCase()
                for ((index, step) in steps.withIndex()) {
                    if (!isActive || emergencyStopped) break
                    while (paused && isActive) delay(50)
                    delay(step.delayBeforeMs)
                    val ok = gestureEngine.perform(step)
                    stateStore.saveSnapshot(running = true, paused = paused, stepIndex = index)
                    logger.d(TAG, "Step ${step.id} performed=$ok")
                }
            } while (loop && !emergencyStopped && isActive)
            stateStore.saveSnapshot(running = false, paused = paused, stepIndex = 0)
        }
    }

    suspend fun enqueueScenario(profile: String) = queue.enqueue(profile)

    @Synchronized fun pause() { paused = true; stateStore.saveSnapshot(isRunning(), true, 0) }
    @Synchronized fun resume() { paused = false; stateStore.saveSnapshot(isRunning(), false, 0) }
    @Synchronized fun emergencyStop() { emergencyStopped = true; stop() }

    @Synchronized
    fun stop() {
        runningJob?.cancel()
        runningJob = null
        paused = false
        stateStore.saveSnapshot(running = false, paused = false, stepIndex = 0)
    }

    fun isRunning(): Boolean = runningJob?.isActive == true

    companion object { private const val TAG = "AutomationRuntime" }
}
