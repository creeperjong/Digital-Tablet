package com.example.digitaltablet.domain.usecase

import android.content.Context
import com.example.digitaltablet.domain.model.llm.Assistant
import com.example.digitaltablet.domain.model.llm.common.FileObj
import com.example.digitaltablet.domain.repository.ILanguageModelRepository
import java.io.File

class LanguageModelUseCase(
    private val languageModelRepository: ILanguageModelRepository
) {
    suspend fun getAssistantList(gptApiKey: String): List<Assistant> {
        return languageModelRepository.listAssistants(
            apiKey = gptApiKey
        ).data
    }

    suspend fun uploadFile(file: File, purpose: String, gptApiKey: String): FileObj {
        return languageModelRepository.uploadFile(
            file = file,
            purpose = purpose,
            apiKey = gptApiKey
        )
    }

    suspend fun retrieveFile(fileId: String, gptApiKey: String): File? {
        return languageModelRepository.retrieveFileContent(
            fileId = fileId,
            apiKey = gptApiKey
        )
    }
}