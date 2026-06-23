package com.autoclicker.automation.runtime.state

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExecutionQueue @Inject constructor() {
    private val queue = ArrayDeque<String>()
    private val mutex = Mutex()

    suspend fun enqueue(profile: String) = mutex.withLock { queue.addLast(profile) }
    suspend fun dequeue(): String? = mutex.withLock { queue.removeFirstOrNull() }
    suspend fun clear() = mutex.withLock { queue.clear() }
}
