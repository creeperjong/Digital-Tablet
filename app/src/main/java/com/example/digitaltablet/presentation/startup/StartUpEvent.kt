package com.example.digitaltablet.presentation.startup

import android.content.Context

sealed class StartUpEvent {

    data object InitRobotList: StartUpEvent()

    data object InitOpenAiInfo: StartUpEvent()

    data class InitSharedPreferences(val context: Context): StartUpEvent()

    data class SetRobotInfo(val robotName: String): StartUpEvent()

    data class SetOrgName(val orgName: String): StartUpEvent()

    data class SetProjName(val projName: String): StartUpEvent()

    data class SetAsstName(val asstName: String): StartUpEvent()
}