package com.example.digitaltablet.presentation.startup

import android.os.Parcelable
import com.example.digitaltablet.BuildConfig
import com.example.digitaltablet.util.Constants
import kotlinx.parcelize.Parcelize

@Parcelize
data class StartUpState(
    val robotName: String = "",
    val deviceId: String = "",
    val robotOptions: Map<String, String> = emptyMap()
): Parcelable
