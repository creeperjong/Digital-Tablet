package com.example.digitaltablet.presentation.tablet.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import coil3.imageLoader
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import coil3.toBitmap
import kotlin.math.min

@Composable
fun ClickableCanvas(
    imageUrl: String,
    tapPositions: List<Offset>,
    tappable: Boolean,
    modifier: Modifier = Modifier,
    onTap: (Offset) -> Unit
) {
    val context = LocalContext.current
    var backgroundImage: ImageBitmap? by remember {
        mutableStateOf(null)
    }

    LaunchedEffect(imageUrl) {
        if (imageUrl == "") {
            backgroundImage = null
        } else {
            val request = ImageRequest.Builder(context)
                .data(imageUrl)
                .build()
            val result = context.imageLoader.execute(request)
            if (result is SuccessResult) {
                backgroundImage = result.image.toBitmap().asImageBitmap()
            }
        }
    }

    Canvas (
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    onTap(offset)
                }
            }
    ) {
        backgroundImage?.let {
            val widthZoomRatio = size.width.toInt() * 1f / it.width
            val heightZoomRatio = size.height.toInt() * 1f / it.height
            val targetZoomRatio = min(widthZoomRatio, heightZoomRatio)
            val dstWidth = (it.width * targetZoomRatio).toInt()
            val dstHeight = (it.height * targetZoomRatio).toInt()
            drawImage(
                image = it,
                srcSize = IntSize(it.width, it.height),
                dstSize = IntSize(width = dstWidth, height = dstHeight),
                dstOffset = IntOffset(size.width.toInt() / 2 - dstWidth / 2, 0)
            )
        }

        if (tappable && imageUrl.isNotBlank()) {
            tapPositions.forEach { position ->
                drawCircle(
                    color = Color.Red,
                    radius = 40f,
                    center = position,
                    style = Stroke(width = 3f)
                )
            }
        }
    }

}