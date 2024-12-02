package com.example.digitaltablet.data.remote.dto.request

import java.io.File

data class UploadFileRequest(
    val file: File,
    val purpose: String,
)
