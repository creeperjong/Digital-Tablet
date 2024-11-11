package com.example.digitaltablet.data.repository

import android.util.Log
import com.example.digitaltablet.data.remote.RcslApi
import com.example.digitaltablet.domain.model.rcsl.Robot
import com.example.digitaltablet.domain.repository.IRcslRepository

class RcslRepository(
    private val rcslApi: RcslApi
): IRcslRepository {

    override suspend fun getRobotList(): List<Robot> {
        return try {
            rcslApi.getRobotList().data
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }


}