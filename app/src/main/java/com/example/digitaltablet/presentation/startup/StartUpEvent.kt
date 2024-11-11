package com.example.digitaltablet.presentation.startup

import android.content.Context

sealed class StartUpEvent {

    data object InitRobotList: StartUpEvent()

    data class InitSharedPreferences(val context: Context): StartUpEvent()

    data class SetRobotInfo(val robotName: String): StartUpEvent()
}