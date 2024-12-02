package com.example.digitaltablet.data.repository

import android.content.Context
import com.example.digitaltablet.data.remote.LanguageModelApi
import com.example.digitaltablet.data.remote.dto.request.UploadFileRequest
import com.example.digitaltablet.domain.model.llm.AssistantList
import com.example.digitaltablet.domain.model.llm.common.FileObj
import com.example.digitaltablet.domain.repository.ILanguageModelRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class LanguageModelRepository(
    private val languageModelApi: LanguageModelApi,
    @ApplicationContext private val context: Context
): ILanguageModelRepository {

    override suspend fun listAssistants(apiKey: String): AssistantList {
        return try {
            languageModelApi.listAssistants(
                apiKey = apiKey
            )
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    override suspend fun uploadFile(file: File, purpose: String, apiKey: String): FileObj {
        return try {
            languageModelApi.uploadFile(
                file = createFilePart(file),
                purpose = purpose.toRequestBody("text/plain".toMediaType()),
                apiKey = apiKey
            )
        } catch(e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    override suspend fun retrieveFileContent(fileId: String, apiKey: String): File? {
        return try {
            val response = languageModelApi.retrieveFileContent(
                fileId = fileId,
                apiKey = apiKey
            )
            if (response.isSuccessful) {
                response.body()?.let { responseBody ->
                    val tempFile = File.createTempFile(fileId, null, context.cacheDir)
                    tempFile.outputStream().use { os ->
                        responseBody.byteStream().use { `is` ->
                            `is`.copyTo(os)
                        }
                    }
                    tempFile
                }
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    private fun createFilePart(file: File): MultipartBody.Part {
        val requestFile = file.asRequestBody("application/octet-stream".toMediaType())
        return MultipartBody.Part.createFormData("file", file.name, requestFile)
    }
}