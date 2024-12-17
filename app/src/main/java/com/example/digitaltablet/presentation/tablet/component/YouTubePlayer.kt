package com.example.digitaltablet.presentation.tablet.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@Composable
fun YouTubePlayer(
    videoUrl: String,
    playerCommand: PlayerCommand,
    modifier: Modifier = Modifier,
    onCommandExecute: () -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val videoId = getYouTubeVideoId(videoUrl)
    var youTubePlayer: YouTubePlayer? by remember { mutableStateOf(null) }

    LaunchedEffect(playerCommand) {
        when(playerCommand) {
            is PlayerCommand.Play -> youTubePlayer?.play()
            is PlayerCommand.Pause -> youTubePlayer?.pause()
            else -> {}
        }
        onCommandExecute()
    }

    AndroidView(
        factory = { context ->
            YouTubePlayerView(context).apply {
                enableAutomaticInitialization = false
                lifecycleOwner.lifecycle.addObserver(this)
                initialize(
                    object : AbstractYouTubePlayerListener() {
                        override fun onReady(player: YouTubePlayer) {
                            youTubePlayer = player
                            player.cueVideo(videoId, 0f)
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

sealed class PlayerCommand {
    data object Play : PlayerCommand()
    data object Pause : PlayerCommand()
    data object None : PlayerCommand()
}