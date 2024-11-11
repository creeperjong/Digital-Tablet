package com.example.digitaltablet.domain.model.rcsl

data class Organization (
    val name: String,
    val project: List<Project>
) {
    data class Project (
        val api_key: String,
        val name: String
    )
}