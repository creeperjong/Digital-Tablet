package com.example.digitaltablet.presentation.tablet.component

import android.content.Context
import android.graphics.Color
import android.widget.TextView
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import com.example.digitaltablet.presentation.Dimens.MediumFontSize
import com.example.digitaltablet.presentation.Dimens.SmallPadding
import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser

@Composable
fun MarkdownText(markdown: String) {
    val textColor = MaterialTheme.colorScheme.onBackground
    val htmlContent = renderMarkdown(markdown)
    AndroidView(
        factory = { context ->
            TextView(context).apply {
                text = HtmlCompat.fromHtml(htmlContent, HtmlCompat.FROM_HTML_MODE_LEGACY)
                textSize = MediumFontSize.value
                setTextColor(textColor.toArgb())
                val paddingPx = SmallPadding.toPx(context)
                setPadding(paddingPx, paddingPx, paddingPx, paddingPx)
            }
        },
        update = { textView ->
            textView.text = HtmlCompat.fromHtml(htmlContent, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }
    )
}

fun renderMarkdown(markdown: String): String {
    val parser = Parser.builder().build()
    val document = parser.parse(markdown)
    val renderer = HtmlRenderer.builder().build()
    return renderer.render(document)
}

fun Dp.toPx(context: Context): Int {
    val density = context.resources.displayMetrics.density
    return (this.value * density).toInt()
}