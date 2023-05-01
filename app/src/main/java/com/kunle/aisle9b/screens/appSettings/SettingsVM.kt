package com.kunle.aisle9b.screens.appSettings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kunle.aisle9b.models.AppSettings
import com.kunle.aisle9b.repository.ShoppingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsVM @Inject constructor(private val repository: ShoppingRepository) : ViewModel() {

    private val _settings = MutableStateFlow<List<AppSettings>>(emptyList())
    val settings = _settings.asStateFlow()

    init {
        viewModelScope.launch(context = Dispatchers.Default) {
            repository.getAllSettings().distinctUntilChanged().collect { listOfSettings ->
                if (listOfSettings.isNotEmpty()) {
                    _settings.value = listOfSettings
                }
            }
        }
    }

    fun insertSettings(settings: AppSettings) =
        viewModelScope.launch { repository.insertSettings(settings) }

    fun deleteSettings(settings: AppSettings) =
        viewModelScope.launch { repository.deleteSettings(settings) }

    fun updateSettings(settings: AppSettings) =
        viewModelScope.launch { repository.updateSettings(settings) }

    fun deleteAllSettings() = viewModelScope.launch { repository.deleteAllSettings() }

    suspend fun checkSetting(name: String): Int {
        return viewModelScope.async(Dispatchers.Default) {
            repository.checkSetting(name)
        }.await()
    }
}