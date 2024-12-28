package com.example.digitaltablet.presentation.tablet

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil3.imageLoader
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import coil3.request.allowHardware
import coil3.toBitmap
import com.example.digitaltablet.domain.model.llm.common.FileObj
import com.example.digitaltablet.domain.usecase.LanguageModelUseCase
import com.example.digitaltablet.domain.usecase.MqttUseCase
import com.example.digitaltablet.domain.usecase.RcslUseCase
import com.example.digitaltablet.presentation.tablet.component.PlayerCommand
import com.example.digitaltablet.util.Constants.Mqtt
import com.example.digitaltablet.util.getPropertyFromJsonString
import com.example.digitaltablet.util.toBitMap
import com.example.digitaltablet.util.toImageBitmap
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min

@HiltViewModel
class TabletViewModel @Inject constructor(
    private val mqttUseCase: MqttUseCase,
    private val languageModelUseCase: LanguageModelUseCase
): ViewModel() {

    private val _state = MutableStateFlow(TabletState())
    val state: StateFlow<TabletState> = _state.asStateFlow()

    fun onEvent(event: TabletEvent) {
        when (event) {
            is TabletEvent.ClearToastMsg -> {
                clearToast()
            }
            is TabletEvent.SetConnectInfos -> {
                setConnectInfos(
                    deviceId = event.deviceId,
                    apiKey = event.apiKey,
                    asstId = event.asstId
                )
            }
            is TabletEvent.ConnectMqttBroker -> {
                connectMqtt()
            }
            is TabletEvent.DisconnectMqttBroker -> {
                disconnectMqtt()
            }
            is TabletEvent.TapOnCanvas -> {
                onCanvasTapped(event.position)
            }
            is TabletEvent.ClearCanvas -> {
                clearCanvas()
            }
            is TabletEvent.ToggleCaptionVisibility -> {
                toggleCaptionVisibility()
            }
            is TabletEvent.ToggleImageVisibility -> {
                toggleImageVisibility()
            }
            is TabletEvent.SwitchImage -> {
                switchImage(event.page)
            }
            is TabletEvent.UploadImage -> {
                sendImage(event.image, event.onUpload)
            }
            is TabletEvent.ConfirmDialog -> {
                sendTextInput(_state.value.dialogTextInput)
                _state.value = _state.value.copy(showTextDialog = false, dialogTextInput = "")
            }
            is TabletEvent.ShowDialog -> {
                _state.value = _state.value.copy(showTextDialog = true)
            }
            is TabletEvent.DismissDialog -> {
                _state.value = _state.value.copy(showTextDialog = false, dialogTextInput = "")
            }
            is TabletEvent.ChangeDialogTextInput -> {
                _state.value = _state.value.copy(dialogTextInput = event.text)
            }
            is TabletEvent.UploadFile -> {
                sendFile(event.file)
            }
            is TabletEvent.ReceiveQrCodeResult -> {
                sendTextInput(event.result)
            }
            is TabletEvent.NavigateUp -> {
                resetAllTempStates()
            }
            is TabletEvent.ChangeCanvasRatio -> {
                _state.value = _state.value.copy(canvasToImageRatio = event.ratio)
            }
            is TabletEvent.SubmitCanvas -> {
                sendCanvas(event.context)
            }
            is TabletEvent.ClearPlayerCommand -> {
                clearPlayerCommand()
            }
        }
    }

    private fun setConnectInfos(deviceId: String, apiKey: String, asstId: String) {
        _state.value = _state.value.copy(
            deviceId = deviceId,
            gptApiKey = apiKey,
            assistantId = asstId
        )
    }

    private fun resetAllTempStates() {
        _state.value = _state.value.copy(
            canvasTapPositions = emptyList(),
            isCanvasTappable = false,
            canvasToImageRatio = 0f,
            isCaptionVisible = true,
            isImageVisible = false,
            displayOn = true,
            keepContentOn = true,
            playerCommand = PlayerCommand.None,
            caption = "",
            responseCaption = "",
            mediaSources = emptyList(),
            mediaIdx = null,
            remoteAccepted = false,
        )
        mqttUseCase.publish(
            topic = getFullTopic(Mqtt.Topic.RESPONSE),
            message = "[END]",
            qos = 0
        )
    }

    /*
     *  UI related functions
     */

    private fun showToast(message: String) {
        val currentMessages = _state.value.toastMessages.toMutableList()
        currentMessages.add(message)
        _state.value = _state.value.copy(toastMessages = currentMessages)
    }

    private fun clearToast() {
        _state.value = _state.value.copy(toastMessages = emptyList())
    }

    private fun onCanvasTapped(position: Offset) {
        _state.value = _state.value.copy(
            canvasTapPositions = _state.value.canvasTapPositions + position
        )
    }

    private fun clearCanvas() {
        _state.value = _state.value.copy(canvasTapPositions = emptyList())
    }

    private fun toggleCaptionVisibility() {
        _state.value = _state.value.copy(isCaptionVisible = !_state.value.isCaptionVisible)
    }

    private fun toggleImageVisibility() {
        _state.value = _state.value.copy(isImageVisible = !_state.value.isImageVisible)
    }

    private fun switchImage(page: Int) {
        val maxPage = _state.value.mediaSources.size
        _state.value = _state.value.copy(
            mediaIdx = max(min(page, maxPage - 1), 0),
            canvasTapPositions = emptyList()
        )
    }

    private suspend fun canvasToFile(
        context: Context,
        backgroundImgUri: String,
        tapPositions: List<Offset>,
    ): File {
        val backgroundImage: Bitmap? = when {
            backgroundImgUri.isBlank() -> null
            backgroundImgUri.startsWith("http") -> {
                val request = ImageRequest.Builder(context)
                    .data(backgroundImgUri)
                    .allowHardware(false)
                    .build()
                val result = context.imageLoader.execute(request)
                if (result !is SuccessResult) null
                else result.image.toBitmap()
            }
            Uri.parse(backgroundImgUri).scheme != null -> {
                Uri.parse(backgroundImgUri).toBitMap(context)
            }
            else -> null
        }

        requireNotNull(backgroundImage) { "Invalid background image URI" }

        val bitmap = Bitmap.createBitmap(
            backgroundImage.width,
            backgroundImage.height,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)

        val paint = Paint()
        canvas.drawBitmap(backgroundImage, 0f, 0f, paint)

        val circlePaint = Paint().apply {
            color = Color.RED
            style = Paint.Style.STROKE
            strokeWidth = 3f
        }
        val imageToCanvasRatio = 1f / _state.value.canvasToImageRatio
        tapPositions.forEach { position ->
            canvas.drawCircle(
                position.x,
                position.y,
                40f * imageToCanvasRatio,
                circlePaint
            )
        }

        val file = File(context.cacheDir, "canvas_output_${System.currentTimeMillis()}.png")
        file.outputStream().use { os ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)
        }

        return file
    }

    private fun playVideo() {
        _state.value = _state.value.copy(playerCommand = PlayerCommand.Play)
    }

    private fun pauseVideo() {
        _state.value = _state.value.copy(playerCommand = PlayerCommand.Pause)
    }

    private fun clearPlayerCommand() {
        _state.value = _state.value.copy(playerCommand = PlayerCommand.None)
    }

    /*
     *  R&T related functions
     */

    private fun sendConnectInfos() {
        val apiKey = _state.value.gptApiKey
        val asstId = _state.value.assistantId
        if (apiKey.isNotBlank()) {
            mqttUseCase.publish(
                topic = getFullTopic(Mqtt.Topic.API_KEY),
                message = apiKey,
                qos = 0
            )
        }
        if (asstId.isNotBlank()) {
            mqttUseCase.publish(
                topic = getFullTopic(Mqtt.Topic.ASST_ID),
                message = asstId,
                qos = 0
            )
        }
    }

    private fun sendImage(image: File?, onSent: (File) -> Unit = {}) {
        if ( image == null ) {
            showToast("Error: Image not found.")
        } else {
            uploadFile(file = image, purpose = "vision") { fileObj ->
                mqttUseCase.publish(
                    topic = getFullTopic(Mqtt.Topic.SEND_IMAGE),
                    message = fileObj.id,
                    qos = 0
                )
                onSent(image)
            }
        }
    }

    private fun sendTextInput(text: String) {
        mqttUseCase.publish(
            topic = getFullTopic(Mqtt.Topic.TEXT_INPUT),
            message = text,
            qos = 0
        )
    }

    private fun sendFile(file: File?) {
        if ( file == null) {
            showToast("Error: File not found")
        } else {
            uploadFile(file = file, purpose = "assistants") { fileObj ->
                val gson = Gson()
                mqttUseCase.publish(
                    topic = getFullTopic(Mqtt.Topic.SEND_FILE),
                    message = gson.toJson(mapOf(
                        "fileid" to fileObj.id,
                        "filename" to fileObj.filename
                    )),
                    qos = 0
                )
            }
        }
    }

    private fun sendCanvas(context: Context) {
        viewModelScope.launch {
            val mediaIdx = _state.value.mediaIdx ?: return@launch
            val media = _state.value.mediaSources[mediaIdx]
            val tapPositions = _state.value.canvasTapPositions

            val file = canvasToFile(
                context = context,
                backgroundImgUri = media,
                tapPositions = tapPositions
            )
            sendImage(file)
            _state.value = _state.value.copy(
                remoteAccepted = false,
                canvasTapPositions = emptyList(),
                isCanvasTappable = false
            )
        }
    }

    /*
     *  LLM related functions
     */

    private fun uploadFile(file: File, purpose: String, onUpload: (FileObj) -> Unit) {
        viewModelScope.launch {
            val fileObj = languageModelUseCase.uploadFile(
                file = file,
                purpose = purpose,
                gptApiKey = _state.value.gptApiKey
            )
            onUpload(fileObj)
        }
    }

    /*
     *  MQTT related functions
     */

    private fun connectMqtt() {
        mqttUseCase.bindService {
            mqttUseCase.connect(
                host = Mqtt.BROKER_URL,
                deviceId = _state.value.deviceId,
                onConnected = {
                    initialSubscription()
                    sendConnectInfos()
                },
                onMessageArrived = { topic, message ->
                    onMqttMessageArrived(topic, message)
                }
            )
        }
    }

    private fun disconnectMqtt() {
        mqttUseCase.disconnect {}
    }

    private fun initialSubscription() {
        mqttUseCase.apply {
            subscribe(getFullTopic(Mqtt.Topic.TTS), 0)
            subscribe(getFullTopic(Mqtt.Topic.STT), 0)
            subscribe(getFullTopic(Mqtt.Topic.IMAGE), 0)
            subscribe(getFullTopic(Mqtt.Topic.TABLET), 0)
            subscribe(getFullTopic(Mqtt.Topic.ARGV), 0)
        }
    }

    private fun getFullTopic(topic: String): String {
        return topic.replace(Regex.escape("{{deviceId}}").toRegex(), _state.value.deviceId)
    }

    private fun onMqttMessageArrived(topic: String, message: String) {
        when (topic) {
            getFullTopic(Mqtt.Topic.TTS) -> {
                pauseVideo()
                val urls = extractUrlsFromText(message)
                val caption = sanitizeTextForCaption(message)
                _state.value = _state.value.copy(caption = caption)
                if (urls.isEmpty()) {
                    if (!_state.value.keepContentOn) {
                        _state.value = _state.value.copy(
                            isImageVisible = false,
                            mediaSources = emptyList(),
                            mediaIdx = null,
                            canvasTapPositions = emptyList()
                        )
                    }
                } else {
                    _state.value = _state.value.copy(
                        isImageVisible = true,
                        mediaSources = urls,
                        mediaIdx = 0,
                        canvasTapPositions = emptyList()
                    )
                }
            }
            getFullTopic(Mqtt.Topic.STT) -> {
                val caption = message.replaceFirstChar { it.uppercase() }
                    .split(": ")
                    .joinToString(": ") { sentence -> sentence.replaceFirstChar { it.uppercase() } }
                _state.value = _state.value.copy(responseCaption = caption)
            }
            getFullTopic(Mqtt.Topic.IMAGE) -> {
                val gson = Gson()
                val type = object : TypeToken<List<String>>() {}.type
                val images: List<String> = gson.fromJson(message, type)
                if (images.isEmpty()) {
                    if (!_state.value.keepContentOn) {
                        _state.value = _state.value.copy(
                            isImageVisible = false,
                            mediaSources = emptyList(),
                            mediaIdx = null,
                            canvasTapPositions = emptyList()
                        )
                    }
                } else {
                    viewModelScope.launch {
                        val mediaSources = mutableListOf<String>()
                        images.forEach {
                            val file = languageModelUseCase.retrieveFile(it, _state.value.gptApiKey)
                            if (file != null) {
                                mediaSources.add(file.toUri().toString())
                            }
                        }
                        _state.value = _state.value.copy(
                            isImageVisible = true,
                            mediaSources = mediaSources,
                            mediaIdx = if (mediaSources.isEmpty()) null else 0,
                            canvasTapPositions = emptyList(),
                        )
                    }
                }
            }
            getFullTopic(Mqtt.Topic.ARGV) -> {
                when (message) {
                    "wait_for_tap" -> {
                        _state.value = _state.value.copy(
                            isCanvasTappable = true,
                            canvasTapPositions = emptyList(),
                            remoteAccepted = true
                        )
                    }
                    "DISPLAY ON" -> {
                        _state.value = _state.value.copy(isCaptionVisible = true, displayOn = true)
                    }
                    "DISPLAY OFF" -> {
                        _state.value = _state.value.copy(isCaptionVisible = false, displayOn = false)
                    }
                    "KEEP_CONTENT ON" -> {
                        _state.value = _state.value.copy(keepContentOn = true)
                    }
                    "KEEP_CONTENT OFF" -> {
                        _state.value = _state.value.copy(keepContentOn = false)
                    }
                }
            }
            getFullTopic(Mqtt.Topic.TABLET) -> {
                if (_state.value.remoteAccepted) {
                    val response = getPropertyFromJsonString(
                        json = message,
                        propertyName = "TABLET",
                        expectedType = String::class
                    )
                    mqttUseCase.publish(
                        topic = getFullTopic(Mqtt.Topic.RESPONSE),
                        message = response ?: "",
                        qos = 0
                    )
                    _state.value = _state.value.copy(
                        isCanvasTappable = false,
                        canvasTapPositions = emptyList(),
                        remoteAccepted = false
                    )
                }
            }
        }
    }

    private fun extractUrlsFromText(text: String): List<String> {
        return Regex("\\((https?://[^\\s)]+)\\)")
            .findAll(text)
            .map { it.groupValues[1] }
            .toList()
    }

    private fun sanitizeTextForCaption(text: String): String {
        val result = text.replace(Regex("\\((https?://[^\\s)]+)\\)"), "")
        return result.replace(Regex("!?\\[[^]]*]"), "")
    }

}