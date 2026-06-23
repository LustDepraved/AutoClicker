package com.autoclicker.domain.usecase

import com.autoclicker.domain.model.ActionStep
import com.autoclicker.domain.model.ActionType
import com.autoclicker.domain.repository.ScenarioRepository
import javax.inject.Inject

class SaveDefaultScenarioUseCase @Inject constructor(
    private val repository: ScenarioRepository
) {
    suspend operator fun invoke() {
        repository.upsertSteps(
            listOf(
                ActionStep(normalizedX = 0.5f, normalizedY = 0.6f, delayBeforeMs = 400, durationMs = 60, type = ActionType.TAP),
                ActionStep(normalizedX = 0.3f, normalizedY = 0.7f, normalizedX2 = 0.7f, normalizedY2 = 0.7f, delayBeforeMs = 250, durationMs = 240, type = ActionType.SWIPE)
            )
        )
    }
}
