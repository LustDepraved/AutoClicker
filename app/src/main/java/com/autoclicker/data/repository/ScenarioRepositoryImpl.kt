package com.autoclicker.data.repository

import com.autoclicker.data.db.ActionStepDao
import com.autoclicker.data.db.ActionStepEntity
import com.autoclicker.domain.model.ActionStep
import com.autoclicker.domain.model.ActionType
import com.autoclicker.domain.repository.ScenarioRepository
import javax.inject.Inject

class ScenarioRepositoryImpl @Inject constructor(
    private val dao: ActionStepDao
) : ScenarioRepository {

    override suspend fun getScenario(): List<ActionStep> = dao.getAll().map {
        ActionStep(
            id = it.id,
            orderInScenario = it.orderInScenario,
            type = ActionType.valueOf(it.type),
            normalizedX = it.normalizedX,
            normalizedY = it.normalizedY,
            normalizedX2 = it.normalizedX2,
            normalizedY2 = it.normalizedY2,
            durationMs = it.durationMs,
            delayBeforeMs = it.delayBeforeMs,
            pressureApprox = it.pressureApprox,
            profile = it.profile
        )
    }

    override suspend fun saveScenario(steps: List<ActionStep>) {
        dao.clearAll()
        dao.insertAll(
            steps.map {
                ActionStepEntity(
                    orderInScenario = it.orderInScenario,
                    type = it.type.name,
                    normalizedX = it.normalizedX,
                    normalizedY = it.normalizedY,
                    normalizedX2 = it.normalizedX2,
                    normalizedY2 = it.normalizedY2,
                    durationMs = it.durationMs,
                    delayBeforeMs = it.delayBeforeMs,
                    pressureApprox = it.pressureApprox,
                    profile = it.profile
                )
            }
        )
    }
}
