package com.autoclicker.core.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.autoclicker.data.db.ActionStepDao
import com.autoclicker.data.db.AppDatabase
import com.autoclicker.data.repository.ScenarioRepositoryImpl
import com.autoclicker.domain.repository.ScenarioRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindScenarioRepository(impl: ScenarioRepositoryImpl): ScenarioRepository
}

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDb(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "autoclicker.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideActionDao(db: AppDatabase): ActionStepDao = db.actionStepDao()

    @Provides
    @Singleton
    fun providePrefs(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences("runtime_state", Context.MODE_PRIVATE)
}
