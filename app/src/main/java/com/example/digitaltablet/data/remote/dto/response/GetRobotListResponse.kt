package com.example.digitaltablet.data.remote.dto.response

import com.example.digitaltablet.domain.model.rcsl.Robot

data class GetRobotListResponse (
    val data: List<Robot>,
    val msg: String,
    val status: Boolean
)