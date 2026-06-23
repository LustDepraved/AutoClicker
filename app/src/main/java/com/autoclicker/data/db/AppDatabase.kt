package com.autoclicker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ActionStepEntity::class], version = 2, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun actionStepDao(): ActionStepDao
}
