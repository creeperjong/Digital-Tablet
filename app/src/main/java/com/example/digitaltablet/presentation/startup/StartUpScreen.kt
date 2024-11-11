package com.example.digitaltablet.presentation.startup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.digitaltablet.presentation.Dimens.MediumPadding
import com.example.digitaltablet.presentation.Dimens.SmallPadding
import com.example.digitaltablet.presentation.robot.component.DropdownMenu
import com.example.digitaltablet.ui.theme.DigitalTabletTheme

@Composable
fun StartUpScreen(
    state: StartUpState,
    onEvent: (StartUpEvent) -> Unit,
    navigateToScanner: () -> Unit,
    navigateToRobot: (StartUpState) -> Unit
) {

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        onEvent(StartUpEvent.InitRobotList)
        onEvent(StartUpEvent.InitSharedPreferences(context))
    }

    Column(
        modifier = Modifier
            .padding(MediumPadding)
            .fillMaxSize()
    ) {

        DropdownMenu(
            label = "Robot",
            text = state.robotName,
            options = state.robotOptions.keys.toList(),
            onSelected = {
                onEvent(StartUpEvent.SetRobotInfo(robotName = it))
            }
        )

        Button(onClick = { navigateToRobot(state) },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            shape = MaterialTheme.shapes.large,
            enabled = state.deviceId.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(SmallPadding)
        ) {
            Text(text = "Done")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StartUpScreenPreview() {
    DigitalTabletTheme {
        StartUpScreen(
            state = StartUpState(),
            onEvent = {},
            navigateToScanner = {},
            navigateToRobot = {}
        )
    }
}