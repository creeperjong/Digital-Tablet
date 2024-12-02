package com.example.digitaltablet.presentation.startup

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.digitaltablet.presentation.Dimens.MediumPadding
import com.example.digitaltablet.presentation.Dimens.SmallPadding
import com.example.digitaltablet.presentation.tablet.component.DropdownMenu
import com.example.digitaltablet.ui.theme.DigitalTabletTheme

@Composable
fun StartUpScreen(
    state: StartUpState,
    onEvent: (StartUpEvent) -> Unit,
    navigateToTablet: (StartUpState) -> Unit
) {

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        onEvent(StartUpEvent.InitRobotList)
        onEvent(StartUpEvent.InitOpenAiInfo)
        onEvent(StartUpEvent.InitSharedPreferences(context))
    }

    Column(
        modifier = Modifier
            .padding(MediumPadding)
            .fillMaxSize()
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(SmallPadding)
        ) {
            Text(
                text = "Select Robot name: ",
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.Start)
            )

            DropdownMenu(
                label = "Robot",
                text = state.robotName,
                options = state.robotOptions.keys.toList(),
                onSelected = {
                    onEvent(StartUpEvent.SetRobotInfo(robotName = it))
                },
                modifier = Modifier.weight(3f)
            )
        }

        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(SmallPadding)
        ) {
            Text(
                text = "Select Organization: ",
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.Start)
            )

            DropdownMenu(
                label = "Organization",
                text = state.orgName,
                options = state.orgOptions,
                onSelected = {
                    onEvent(StartUpEvent.SetOrgName(orgName = it))
                },
                modifier = Modifier.weight(3f)
            )
        }

        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(SmallPadding)
        ) {
            Text(
                text = "Select Project: ",
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.Start)
            )

            DropdownMenu(
                label = "Project",
                text = state.projName,
                options = state.projOptions.keys.toList(),
                onSelected = {
                    onEvent(StartUpEvent.SetProjName(projName = it))
                },
                modifier = Modifier.weight(3f)
            )
        }

        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(SmallPadding)
        ) {
            Text(
                text = "Select AI Agent: ",
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.Start)
            )

            DropdownMenu(
                label = "Agent",
                text = state.asstName,
                options = state.asstOptions.keys.toList(),
                onSelected = {
                    onEvent(StartUpEvent.SetAsstName(asstName = it))
                },
                modifier = Modifier.weight(3f)
            )
        }


        Button(onClick = { navigateToTablet(state) },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            shape = MaterialTheme.shapes.large,
            enabled = state.deviceId.isNotBlank() &&
                    state.asstName.isNotBlank() &&
                    state.asstId.isNotBlank(),
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
            navigateToTablet = {}
        )
    }
}