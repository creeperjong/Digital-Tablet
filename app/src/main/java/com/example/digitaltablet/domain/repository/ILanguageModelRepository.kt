package com.example.digitaltablet.domain.repository

import com.example.digitaltablet.domain.model.llm.AssistantList
import com.example.digitaltablet.domain.model.llm.common.FileObj
import java.io.File

interface ILanguageModelRepository {

    suspend fun listAssistants(
        apiKey: String
    ): AssistantList

    suspend fun uploadFile(
        file: File,
        purpose: String,
        apiKey: String
    ): FileObj

}