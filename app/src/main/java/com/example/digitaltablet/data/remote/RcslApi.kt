package com.example.digitaltablet.data.remote

import com.example.digitaltablet.data.remote.dto.response.GetOpenAiInfoResponse
import com.example.digitaltablet.data.remote.dto.response.GetRobotListResponse
import retrofit2.http.GET

interface RcslApi {

    @GET("robot")
    suspend fun getRobotList(): GetRobotListResponse

    @GET("openai")
    suspend fun getOpenAiInfo(): GetOpenAiInfoResponse

}