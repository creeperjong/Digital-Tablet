package com.example.digitaltablet.data.remote

import com.example.digitaltablet.data.remote.dto.response.GetRobotListResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RcslApi {

    @GET("robot")
    suspend fun getRobotList(): GetRobotListResponse

}