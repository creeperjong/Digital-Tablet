package com.example.digitaltablet.domain.repository

import com.example.digitaltablet.domain.model.llm.AssistantList

interface ILanguageModelRepository {

    suspend fun listAssistants(
        apiKey: String
    ): AssistantList

}