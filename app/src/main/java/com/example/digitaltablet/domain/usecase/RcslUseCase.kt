package com.example.digitaltablet.domain.usecase

import android.util.Log
import com.example.digitaltablet.domain.model.rcsl.Organization
import com.example.digitaltablet.domain.model.rcsl.Robot
import com.example.digitaltablet.domain.repository.IRcslRepository
import com.example.digitaltablet.util.getValueFromLinkedTreeMap
import com.google.gson.internal.LinkedTreeMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class RcslUseCase(
    private val rcslRepository: IRcslRepository
) {
    suspend fun getRobotList(): List<Robot> {
        return rcslRepository.getRobotList()
    }

    suspend fun getOpenAiInfo(): List<Organization> {
        return rcslRepository.getOpenAiInfo()
    }
}