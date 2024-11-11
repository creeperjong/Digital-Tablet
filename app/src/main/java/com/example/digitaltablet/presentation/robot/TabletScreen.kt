package com.example.digitaltablet.presentation.robot

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.example.digitaltablet.util.ToastManager

@Composable
fun RobotScreen(
    state: TabletState,
    onEvent: (TabletEvent) -> Unit
) {
    val context = LocalContext.current

    state.toastMessages.let {
        if (it.isNotEmpty()) {
            for (msg in it){
                ToastManager.showToast(context, msg)
            }
            onEvent(TabletEvent.ClearToastMsg)
        }
    }

    LaunchedEffect(Unit) {
        onEvent(TabletEvent.ConnectMqttBroker)
    }

    DisposableEffect(Unit) {
        onDispose {
            onEvent(TabletEvent.DisconnectMqttBroker)
        }
    }


}

