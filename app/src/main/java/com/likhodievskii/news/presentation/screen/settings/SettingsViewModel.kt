package com.likhodievskii.news.presentation.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.likhodievskii.news.domain.entity.Interval
import com.likhodievskii.news.domain.entity.Language
import com.likhodievskii.news.domain.usecase.settings.GetSettingsUseCase
import com.likhodievskii.news.domain.usecase.settings.UpdateIntervalUseCase
import com.likhodievskii.news.domain.usecase.settings.UpdateLanguageUseCase
import com.likhodievskii.news.domain.usecase.settings.UpdateNotificationsEnabledUseCase
import com.likhodievskii.news.domain.usecase.settings.UpdateWifiOnlyUseCase
import com.likhodievskii.news.presentation.mapper.toSettingsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    getSettingsUseCase: GetSettingsUseCase,
    private val updateLanguageUseCase: UpdateLanguageUseCase,
    private val updateIntervalUseCase: UpdateIntervalUseCase,
    private val updateNotificationsEnabledUseCase: UpdateNotificationsEnabledUseCase,
    private val updateWifiOnlyUseCase: UpdateWifiOnlyUseCase
) : ViewModel() {
    private val _state: MutableStateFlow<SettingsState> = MutableStateFlow(SettingsState.Initial)
    val state = _state.asStateFlow()

    init {
        getSettingsUseCase()
            .onEach { settings ->
                _state.update {
                    settings.toSettingsState()
                }
            }.launchIn(viewModelScope)
    }

    fun processCommand(command: SettingsCommand) {
        viewModelScope.launch {

            when (command) {
                is SettingsCommand.SetNotificationsEnabled -> {
                    updateNotificationsEnabledUseCase(command.enabled)
                }

                is SettingsCommand.SetWifiOnly -> {
                    updateWifiOnlyUseCase(command.wifiOnly)
                }

                is SettingsCommand.SelectLanguage -> {
                    updateLanguageUseCase(command.language)
                }

                is SettingsCommand.SelectInterval -> {
                    updateIntervalUseCase(command.interval)
                }
            }
        }
    }
}


sealed interface SettingsCommand {
    data class SelectLanguage(val language: Language) : SettingsCommand
    data class SelectInterval(val interval: Interval) : SettingsCommand
    data class SetNotificationsEnabled(val enabled: Boolean) : SettingsCommand
    data class SetWifiOnly(val wifiOnly: Boolean) : SettingsCommand
}

sealed interface SettingsState {

    data object Initial : SettingsState
    data class Configuration(
        val selectedLanguage: Language,
        val selectedInterval: Interval,
        val notificationsEnabled: Boolean,
        val wifiOnly: Boolean,
        val languages: List<Language> = Language.entries,
        val intervals: List<Interval> = Interval.entries
    ) : SettingsState
}
