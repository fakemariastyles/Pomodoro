package com.mmb.clock.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mmb.clock.R
import com.mmb.clock.entity.PomodoroScreenEntity
import com.mmb.clock.viewmodel.PomodoroClockViewModel
import com.mmb.ui_compose.component.pom.ControlButton
import com.mmb.ui_compose.component.pom.PomodoroClock

@Composable
fun Clock(
    navigateToSettings: () -> Unit,
    duration: Int,
    viewModel: PomodoroClockViewModel,
    onTimerCompleted: () -> Unit
) {
    viewModel.subscribe(duration)
    Clock(viewModel = viewModel, navigateToSettings, onTimerCompleted)
}

@Composable
internal fun Clock(
    viewModel: PomodoroClockViewModel,
    navigateToSettings: () -> Unit,
    onTimerCompleted: () -> Unit
) {
    val timerState = viewModel.timer.observeAsState()
    val timerRunning = viewModel.timerRunning.observeAsState()
    val progress = viewModel.progress.observeAsState()
    PomScreen(
        PomodoroScreenEntity(
            sessionName = viewModel.sessionName.collectAsState(initial = "").value,
            text = timerState.value?.toString() ?: "",
            numberOfPoms = 3,
            pomsCompleted = 2,
            timerRunning = timerRunning.value ?: false,
            progress = progress.value ?: 0f,
        ),
        navigateToSettings,
        { viewModel.startTimer(onTimerCompleted) },
        viewModel::pauseTimer,
        viewModel::restartTimer,
        viewModel
    )
}

@Composable
fun PomScreen(
    entity: PomodoroScreenEntity,
    navigateToSettings: () -> Unit,
    onStartClicked: () -> Unit,
    onPauseClicked: () -> Unit,
    onRestartClicked: () -> Unit,
    viewModel: PomodoroClockViewModel,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Filled.Settings,
                contentDescription = stringResource(id = R.string.action_setting),
                tint = MaterialTheme.colors.primary,
                modifier = Modifier.clickable { navigateToSettings() }
            )
            Text(
                text = entity.sessionName,
                modifier = Modifier.padding(horizontal = 8.dp),
                fontSize = 25.sp
            )
        }
        Box {
            PomodoroClock(
                text = entity.text,
                progress = entity.progress,
                modifier = Modifier
                    .width(300.dp)
                    .aspectRatio(1f)
            )
        }
        ControlButton(
            running = entity.timerRunning,
            onResumeClicked = onStartClicked,
            onRestartClicked = onRestartClicked,
            onPauseClicked = onPauseClicked,
            modifier = Modifier.size(45.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))
    }
}
