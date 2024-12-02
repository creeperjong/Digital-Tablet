package com.example.digitaltablet.domain.model.llm.common

data class FileObj(
    val id: String,
    val bytes: Int,
    val created_at: Int,
    val filename: String,
    val `object`: String,
    val purpose: String,
)
