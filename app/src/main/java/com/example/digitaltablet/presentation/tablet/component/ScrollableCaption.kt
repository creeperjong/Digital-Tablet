package com.example.digitaltablet.presentation.tablet.component

import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.digitaltablet.presentation.Dimens.MediumFontSize
import com.example.digitaltablet.presentation.Dimens.SmallPadding

@Composable
fun ScrollableCaption(
    caption: String
) {

    val scrollState = rememberScrollState()
    val interactionSource = remember { MutableInteractionSource() }
    var isUserScrolling by remember { mutableStateOf(false) }

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            if (interaction is DragInteraction) {
                isUserScrolling = true
            }
        }
    }

    LaunchedEffect(caption) {
        isUserScrolling = false
        kotlinx.coroutines.delay(3000)
        while (!isUserScrolling && scrollState.value < scrollState.maxValue) {
            scrollState.scrollTo(scrollState.value + 1)
            kotlinx.coroutines.delay(16L)
        }
    }

    Box(modifier = Modifier.verticalScroll(scrollState)) {
        MarkdownText(markdown = caption)
    }
}