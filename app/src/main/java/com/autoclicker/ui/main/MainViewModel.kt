package com.autoclicker.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autoclicker.domain.usecase.SaveDefaultScenarioUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val saveDefaultScenarioUseCase: SaveDefaultScenarioUseCase
) : ViewModel() {
    fun seedScenario() {
        viewModelScope.launch { saveDefaultScenarioUseCase() }
    }
}
