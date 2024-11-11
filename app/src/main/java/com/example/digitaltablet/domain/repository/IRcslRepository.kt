package com.example.digitaltablet.domain.repository

import com.example.digitaltablet.domain.model.rcsl.Organization
import com.example.digitaltablet.domain.model.rcsl.Robot

interface IRcslRepository {

    suspend fun getRobotList(): List<Robot>

    suspend fun getOpenAiInfo(): List<Organization>

}