package com.example.digitaltablet.presentation.startup

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitaltablet.domain.usecase.LanguageModelUseCase
import com.example.digitaltablet.domain.usecase.RcslUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartUpViewModel @Inject constructor(
    private val languageModelUseCase: LanguageModelUseCase,
    private val rcslUseCase: RcslUseCase
): ViewModel() {

    private val _state = MutableStateFlow(StartUpState())
    val state: StateFlow<StartUpState> = _state.asStateFlow()

    private lateinit var sharedPreferences: SharedPreferences

    fun onEvent(event: StartUpEvent) {
        when(event) {
            is StartUpEvent.InitRobotList -> {
                initRobotList()
            }
            is StartUpEvent.InitOpenAiInfo -> {
                initOpenAiInfo()
            }
            is StartUpEvent.InitSharedPreferences -> {
                initSharedPreferences(event.context)
            }
            is StartUpEvent.SetRobotInfo -> {
                setRobotInfo(event.robotName)
            }
            is StartUpEvent.SetOrgName -> {
                setOrgName(event.orgName)
            }
            is StartUpEvent.SetProjName -> {
                setProjName(event.projName)
            }
            is StartUpEvent.SetAsstName -> {
                setAsstName(event.asstName)
            }
        }
    }

    private fun initRobotList() {
        viewModelScope.launch {
            val robots = rcslUseCase.getRobotList().associate { it.robot_name to it.serial_number }
            _state.value = _state.value.copy(
                deviceId = robots[_state.value.robotName] ?: "",
                robotOptions = robots
            )
        }
    }

    private fun initOpenAiInfo() {
        viewModelScope.launch {
            val info = rcslUseCase.getOpenAiInfo().associate { org ->
                org.name to org.project.associate { it.name to it.api_key }
            }
            val orgName = _state.value.orgName
            val projName = _state.value.projName
            val asstName = _state.value.asstName

            _state.value = _state.value.copy(
                projApiKey = decodeApiKey(info[orgName]?.get(projName) ?: ""),
                orgOptions = info.keys.toList(),
                projOptions = info[orgName] ?: emptyMap(),
                openAiInfo = info,
            )

            if (orgName.isNotBlank() && projName.isNotBlank() && asstName.isNotBlank()) {
                val apiKey = _state.value.projApiKey
                val assistants = languageModelUseCase.getAssistantList(
                    gptApiKey = apiKey
                )
                _state.value = _state.value.copy(
                    asstId = assistants.find { it.name == asstName }?.id ?: "",
                    asstOptions = assistants.associate { (it.name ?: "Unknown agent") to it.id }
                )
            }
        }
    }

    private fun initSharedPreferences(context: Context) {
        sharedPreferences = context.getSharedPreferences("connectInfo", Context.MODE_PRIVATE)
        loadSharedPreferences()
    }

    private fun setRobotInfo(robotName: String) {
        _state.value = _state.value.copy(
            robotName = robotName,
            deviceId = _state.value.robotOptions[robotName] ?: ""
        )
        saveSharedPreferences("robotName", robotName)
    }

    private fun setOrgName(orgName: String) {
        _state.value = _state.value.copy(
            orgName = orgName,
            projName = "",
            projApiKey = "",
            asstName = "",
            asstId = "",
            projOptions = _state.value.openAiInfo[orgName] ?: emptyMap(),
        )
        saveSharedPreferences("orgName", orgName)
    }

    private fun setProjName(projName: String) {
        val apiKey = decodeApiKey(_state.value.projOptions[projName] ?: "")
        viewModelScope.launch {
            val assistants = languageModelUseCase.getAssistantList(
                gptApiKey = apiKey
            )
            _state.value = _state.value.copy(
                projName = projName,
                projApiKey = apiKey,
                asstName = "",
                asstId = "",
                asstOptions = assistants.associate { (it.name ?: "Unknown agent") to it.id }
            )
        }
        saveSharedPreferences("projName", projName)
    }

    private fun setAsstName(asstName: String) {
        _state.value = _state.value.copy(
            asstName = asstName,
            asstId = _state.value.asstOptions[asstName] ?: ""
        )
        saveSharedPreferences("asstName", asstName)
    }

    private fun loadSharedPreferences() {
        _state.value = _state.value.copy(
            robotName = sharedPreferences.getString("robotName", "") ?: "",
            orgName = sharedPreferences.getString("orgName", "") ?: "",
            projName = sharedPreferences.getString("projName", "") ?: "",
            asstName = sharedPreferences.getString("asstName", "") ?: ""
        )
    }

    private fun saveSharedPreferences(varName: String, value: String) {
        sharedPreferences.edit().putString(varName, value).apply()
    }

    private fun decodeApiKey(apiKey: String): String {
        return if (apiKey.length >= 6) {
            apiKey.removeRange(apiKey.length - 6, apiKey.length - 5)
        } else {
            apiKey
        }
    }

}