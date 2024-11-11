package com.example.digitaltablet.presentation.startup

import android.os.Parcelable
import com.example.digitaltablet.BuildConfig
import com.example.digitaltablet.util.Constants
import kotlinx.parcelize.Parcelize

@Parcelize
data class StartUpState(
    val robotName: String = "",
    val deviceId: String = "",
    val orgName: String = "",
    val projName: String = "",
    val projApiKey: String = "",
    val asstName: String = "",
    val asstId: String = "",

    val robotOptions: Map<String, String> = emptyMap(),
    val orgOptions: List<String> = emptyList(),
    val projOptions: Map<String, String> = emptyMap(),
    val asstOptions: Map<String, String> = emptyMap(),
    val openAiInfo: Map<String, Map<String, String>> = emptyMap(),
): Parcelable
