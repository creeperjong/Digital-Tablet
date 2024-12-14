package com.example.digitaltablet.presentation.tablet.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@Composable
fun YouTubePlayer(videoUrl: String, modifier: Modifier = Modifier) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val videoId = getYouTubeVideoId(videoUrl)

    AndroidView(
        factory = { context ->
            YouTubePlayerView(context).apply {
                enableAutomaticInitialization = false
                lifecycleOwner.lifecycle.addObserver(this)
                initialize(
                    object : AbstractYouTubePlayerListener() {
                        override fun onReady(youTubePlayer: YouTubePlayer) {
                            youTubePlayer.cueVideo(videoId, 0f)
                        }
                    },
                    false,
                    IFramePlayerOptions
                        .Builder()
                        .autoplay(0)
                        .mute(0)
                        .controls(0)
                        .build()
                )
            }
        },
        modifier = modifier
    )
}

private fun getYouTubeVideoId(url: String): String {
    val regex = "(?:https?://)?(?:www\\.)?(?:youtube\\.com/(?:watch\\?v=|v/|shorts/|embed/|.*\\?v=)|youtu\\.be/)([\\w\\-]{11})".toRegex()
    val matchResult = regex.find(url)
    return matchResult?.groups?.get(1)?.value ?: ""
}