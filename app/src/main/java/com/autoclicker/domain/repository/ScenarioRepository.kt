package com.autoclicker.domain.repository

import com.autoclicker.domain.model.ActionStep

interface ScenarioRepository {
    suspend fun upsertSteps(steps: List<ActionStep>)
    suspend fun loadSteps(): List<ActionStep>
}
