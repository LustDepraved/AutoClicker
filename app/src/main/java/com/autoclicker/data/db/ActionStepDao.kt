package com.autoclicker.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ActionStepDao {
    @Query("SELECT * FROM action_steps ORDER BY id ASC")
    suspend fun getAll(): List<ActionStepEntity>

    @Query("DELETE FROM action_steps")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(steps: List<ActionStepEntity>)
}
