package com.example.digitaltablet.presentation.robot

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.digitaltablet.R
import com.example.digitaltablet.presentation.Dimens.LargeFontSize
import com.example.digitaltablet.presentation.Dimens.MediumFontSize
import com.example.digitaltablet.presentation.Dimens.MediumPadding
import com.example.digitaltablet.presentation.Dimens.SmallFontSize
import com.example.digitaltablet.presentation.Dimens.SmallPadding
import com.example.digitaltablet.util.ToastManager

@Composable
fun TabletScreen(
    state: TabletState,
    onEvent: (TabletEvent) -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

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
                Image(
                    painter = painterResource(id = R.drawable.ic_kebbi),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(SmallPadding)
                        .weight(1f)
                        .clickable { }
                )

                IconButton(
                    modifier = Modifier
                        .padding(SmallPadding)
                        .fillMaxWidth()
                        .weight(1f),
                    onClick = { /*TODO*/ }
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

            Column (
                modifier = Modifier
                    .padding(SmallPadding)
                    .fillMaxHeight()
                    .weight(4f)
                    .verticalScroll(scrollState)
            ) {
                Text(
                    text = state.caption,
                    fontSize = MediumFontSize,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(SmallPadding)
                )
            }

            Column (
                modifier = Modifier
                    .padding(SmallPadding)
                    .fillMaxHeight()
                    .weight(4f)
            ) {
                Text(
                    text = state.mediaUrl,
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

                }
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Button(onClick = { /* TODO */ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        shape = MaterialTheme.shapes.large,
                        modifier = Modifier
                            .padding(end = SmallPadding)
                            .fillMaxHeight()
                            .weight(1f)
                    ) {
                        Text(text = "<", fontSize = MediumFontSize)
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
                    Button(onClick = { /* TODO */ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        shape = MaterialTheme.shapes.large,
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

        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Button(onClick = { /* TODO */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = MaterialTheme.shapes.large,
                modifier = Modifier
                    .padding(SmallPadding)
                    .fillMaxHeight()
                    .weight(1f)
            ) {
                Text(text = "Hide Textbox", fontSize = LargeFontSize)
            }

            Button(onClick = { /* TODO */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = MaterialTheme.shapes.large,
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