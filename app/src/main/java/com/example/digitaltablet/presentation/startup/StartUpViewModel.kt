package com.example.digitaltablet.presentation.startup

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitaltablet.domain.usecase.RcslUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartUpViewModel @Inject constructor(
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
            is StartUpEvent.InitSharedPreferences -> {
                initSharedPreferences(event.context)
            }
            is StartUpEvent.SetRobotInfo -> {
                setRobotInfo(event.robotName)
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

    private fun initSharedPreferences(context: Context) {
        sharedPreferences = context.getSharedPreferences("robotName", Context.MODE_PRIVATE)
        loadRobotName()
    }

    private fun setRobotInfo(robotName: String) {
        _state.value = _state.value.copy(
            robotName = robotName,
            deviceId = _state.value.robotOptions[robotName] ?: ""
        )
        saveRobotName(robotName)
    }

    private fun loadRobotName() {
        _state.value = _state.value.copy(
            robotName = sharedPreferences.getString("robotName", "") ?: ""
        )
    }

    private fun saveRobotName(name: String) {
        sharedPreferences.edit().putString("robotName", name).apply()
    }

}