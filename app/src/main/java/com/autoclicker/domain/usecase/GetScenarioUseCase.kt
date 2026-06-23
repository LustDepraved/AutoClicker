package com.autoclicker.domain.usecase

import com.autoclicker.domain.model.ActionStep
import com.autoclicker.domain.repository.ScenarioRepository
import javax.inject.Inject

class GetScenarioUseCase @Inject constructor(
    private val repository: ScenarioRepository
) {
    suspend operator fun invoke(): List<ActionStep> = repository.loadSteps()
}
