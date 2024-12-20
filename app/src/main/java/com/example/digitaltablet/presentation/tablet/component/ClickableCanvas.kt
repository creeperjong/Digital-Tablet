package com.example.digitaltablet.presentation.tablet.component

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.util.Size
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.toOffset
import coil3.compose.rememberAsyncImagePainter
import coil3.imageLoader
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import coil3.toBitmap
import com.example.digitaltablet.util.toImageBitmap
import kotlin.math.min

@Composable
fun ClickableCanvas(
    imageUri: String,
    tapPositions: List<Offset>,
    tappable: Boolean,
    modifier: Modifier = Modifier,
    onTap: (Offset) -> Unit,
    onRatioChanged: (Float) -> Unit
) {
    val context = LocalContext.current
    var backgroundImage: ImageBitmap? by remember {
        mutableStateOf(null)
    }
    var zoomRatio by remember { mutableFloatStateOf(0f) }
    var canvasSize by remember { mutableStateOf(Size(0,0)) }
    var imageSize by remember { mutableStateOf(Size(0, 0)) }
    var zoomedImageSize by remember { mutableStateOf(Size(0, 0)) }
    var imageOffset by remember { mutableStateOf(Offset(0f, 0f)) }

    LaunchedEffect(imageUri) {
        if (imageUri == "") {
            backgroundImage = null
        } else if (imageUri.startsWith("http")) {
            val request = ImageRequest.Builder(context)
                .data(imageUri)
                .build()
            val result = context.imageLoader.execute(request)
            if (result is SuccessResult) {
                backgroundImage = result.image.toBitmap().asImageBitmap()
            }
        } else if ( Uri.parse(imageUri).scheme != null ) {
            backgroundImage = Uri.parse(imageUri).toImageBitmap(context)
        }
    }

    Canvas (
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    if ((offset.x.toInt() in imageOffset.x.toInt() until imageOffset.x.toInt() + zoomedImageSize.width) &&
                        offset.y.toInt() in imageOffset.y.toInt() until imageOffset.y.toInt() + zoomedImageSize.height ) {
                        onTap((offset - imageOffset) / zoomRatio)
                    }
                }
            }
            .onGloballyPositioned { layoutCoordinates ->
                val size = layoutCoordinates.size
                canvasSize = Size(size.width, size.height)
            }
    ) {
        backgroundImage?.let {
            imageSize = Size(it.width, it.height)
            val widthZoomRatio = canvasSize.width * 1f / imageSize.width
            val heightZoomRatio = canvasSize.height * 1f / imageSize.height
            zoomRatio = min(widthZoomRatio, heightZoomRatio)
            onRatioChanged(zoomRatio)

            val dstWidth = (imageSize.width * zoomRatio).toInt()
            val dstHeight = (imageSize.height * zoomRatio).toInt()
            zoomedImageSize = Size(dstWidth, dstHeight)
            imageOffset = Offset(
                (canvasSize.width - dstWidth) / 2f,
                (canvasSize.height - dstHeight) / 2f
            )

            drawImage(
                image = it,
                srcSize = IntSize(imageSize.width, imageSize.height),
                dstSize = IntSize(width = zoomedImageSize.width, height = zoomedImageSize.height),
                dstOffset = IntOffset(imageOffset.x.toInt(), imageOffset.y.toInt())
            )
        }

        if (tappable && imageUri.isNotBlank()) {
            tapPositions.forEach { position ->
                drawCircle(
                    color = Color.Red,
                    radius = 40f,
                    center = position * zoomRatio + imageOffset,
                    style = Stroke(width = 3f)
                )
            }
        }
    }
}