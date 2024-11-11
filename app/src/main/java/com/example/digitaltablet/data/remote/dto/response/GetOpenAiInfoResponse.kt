package com.example.digitaltablet.data.remote.dto.response

import com.example.digitaltablet.domain.model.rcsl.Organization

data class GetOpenAiInfoResponse (
    val data: Data,
    val msg: String,
    val status: String
) {
    data class Data (
        val organization: List<Organization>
    )

}
