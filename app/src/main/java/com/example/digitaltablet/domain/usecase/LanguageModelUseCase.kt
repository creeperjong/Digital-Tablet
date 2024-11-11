package com.example.digitaltablet.domain.usecase

import com.example.digitaltablet.domain.model.llm.Assistant
import com.example.digitaltablet.domain.repository.ILanguageModelRepository

class LanguageModelUseCase(
    private val languageModelRepository: ILanguageModelRepository
) {
    suspend fun getAssistantList(gptApiKey: String): List<Assistant> {
        return languageModelRepository.listAssistants(
            apiKey = gptApiKey
        ).data
    }
}