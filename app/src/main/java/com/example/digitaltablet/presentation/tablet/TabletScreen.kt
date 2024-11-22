package com.example.digitaltablet.presentation.tablet

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.digitaltablet.R
import com.example.digitaltablet.presentation.Dimens.LargeFontSize
import com.example.digitaltablet.presentation.Dimens.MediumFontSize
import com.example.digitaltablet.presentation.Dimens.SmallFontSize
import com.example.digitaltablet.presentation.Dimens.SmallPadding
import com.example.digitaltablet.presentation.tablet.component.ClickableCanvas
import com.example.digitaltablet.presentation.tablet.component.ScrollableCaption
import com.example.digitaltablet.util.ToastManager

@Composable
fun TabletScreen(
    state: TabletState,
    onEvent: (TabletEvent) -> Unit
) {
    val context = LocalContext.current
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if ( uri == null ) return@rememberLauncherForActivityResult
        val contentResolver = context.contentResolver
        val mimeType = contentResolver.getType(uri)
        if ( mimeType?.startsWith("image/") == true) {
            onEvent(TabletEvent.UploadImage(uri))
        } else {
            onEvent(TabletEvent.UploadImage(null))
        }
    }

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

    Column (
        modifier = Modifier
            .padding(SmallPadding)
            .fillMaxSize()
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .weight(7f)
        ) {
            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(SmallPadding)
                    .fillMaxHeight()
                    .weight(1f)
            ) {
                IconButton(
                    modifier = Modifier
                        .padding(SmallPadding)
                        .fillMaxWidth()
                        .weight(1f),
                    onClick = { /*TODO*/ }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_kebbi),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                IconButton(
                    modifier = Modifier
                        .padding(SmallPadding)
                        .fillMaxWidth()
                        .weight(1f),
                    onClick = { imagePickerLauncher.launch("image/*") }
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_image),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                IconButton(
                    modifier = Modifier
                        .padding(SmallPadding)
                        .fillMaxWidth()
                        .weight(1f),
                    onClick = { /*TODO*/ }
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_camera),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                IconButton(
                    modifier = Modifier
                        .padding(SmallPadding)
                        .fillMaxWidth()
                        .weight(1f),
                    onClick = { /*TODO*/ }
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_keyboard),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                IconButton(
                    modifier = Modifier
                        .padding(SmallPadding)
                        .fillMaxWidth()
                        .weight(1f),
                    onClick = { /*TODO*/ }
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_attachment),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                IconButton(
                    modifier = Modifier
                        .padding(SmallPadding)
                        .fillMaxWidth()
                        .weight(1f),
                    onClick = { /*TODO*/ }
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_qrcode),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            if (state.isCaptionVisible) {
                Column (
                    modifier = Modifier
                        .padding(SmallPadding)
                        .fillMaxHeight()
                        .weight(
                            if (state.isImageVisible) 4f
                            else 8f
                        )
                ) {
                    ScrollableCaption(caption = state.caption)
                }
            }

            if (state.isImageVisible) {
                Column (
                    modifier = Modifier
                        .padding(SmallPadding)
                        .fillMaxHeight()
                        .weight(
                            if (state.isCaptionVisible) 4f
                            else 8f
                        )
                ) {
                    Text(
                        text =
                        if (state.imageIdx != null)
                            state.imageSources[state.imageIdx]
                        else
                            "",
                        fontSize = SmallFontSize,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .padding(top = SmallPadding)
                            .fillMaxWidth()
                            .weight(1f)
                    )
                    Box(
                        modifier = Modifier
                            .padding(top = SmallPadding, bottom = SmallPadding)
                            .fillMaxWidth()
                            .weight(8f)
                    ) {
                        ClickableCanvas(
                            imageUri =
                            if (state.imageIdx != null)
                                state.imageSources[state.imageIdx]
                            else
                                "",
                            tapPositions = state.canvasTapPositions,
                            tappable = state.isCanvasTappable,
                            modifier = Modifier.fillMaxSize()
                        ) { tapPosition ->
                            onEvent(TabletEvent.TapOnCanvas(tapPosition))
                        }
                    }
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        Button(
                            onClick = {
                                onEvent(TabletEvent.SwitchImage(
                                    page = state.imageIdx?.minus(1) ?: 0)
                                )
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            shape = MaterialTheme.shapes.large,
                            enabled = state.imageIdx in 1 until state.imageSources.size ,
                            modifier = Modifier
                                .padding(end = SmallPadding)
                                .fillMaxHeight()
                                .weight(1f)
                        ) {
                            Text(text = "<", fontSize = MediumFontSize)
                        }
                        Button(onClick = { onEvent(TabletEvent.ClearCanvas) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            shape = MaterialTheme.shapes.large,
                            modifier = Modifier
                                .padding(horizontal = SmallPadding)
                                .fillMaxHeight()
                                .weight(3f)
                        ) {
                            Text(text = "Clear", fontSize = MediumFontSize)
                        }
                        Button(onClick = { /* TODO */ },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            shape = MaterialTheme.shapes.large,
                            modifier = Modifier
                                .padding(horizontal = SmallPadding)
                                .fillMaxHeight()
                                .weight(3f)
                        ) {
                            Text(text = "Submit", fontSize = MediumFontSize)
                        }
                        Button(onClick = {
                            onEvent(TabletEvent.SwitchImage(
                                page = state.imageIdx?.plus(1) ?: 0)
                            )
                        },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            shape = MaterialTheme.shapes.large,
                            enabled = state.imageIdx in 0 until state.imageSources.size - 1,
                            modifier = Modifier
                                .padding(start = SmallPadding)
                                .fillMaxHeight()
                                .weight(1f)
                        ) {
                            Text(text = ">", fontSize = MediumFontSize)
                        }
                    }
                }

            }

        }

        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Button(onClick = { onEvent(TabletEvent.ToggleCaptionVisibility) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = MaterialTheme.shapes.large,
                enabled = state.isImageVisible,
                modifier = Modifier
                    .padding(SmallPadding)
                    .fillMaxHeight()
                    .weight(1f)
            ) {
                Text(text = "Hide Textbox", fontSize = LargeFontSize)
            }

            Button(onClick = { onEvent(TabletEvent.ToggleImageVisibility) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = MaterialTheme.shapes.large,
                enabled = state.isCaptionVisible,
                modifier = Modifier
                    .padding(SmallPadding)
                    .fillMaxHeight()
                    .weight(1f)
            ) {
                Text(text = "Hide Image", fontSize = LargeFontSize)
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = state.responseCaption,
                fontSize = LargeFontSize,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.error
            )
        }
    }

}

@Preview
@Composable
fun PreviewScreen() {
    TabletScreen(state = TabletState()) {

    }
}