package com.mmb.clock

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mmb.core.SettingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PomodoroClockViewModel @Inject constructor(
    private val repository: SettingRepository,
    private val timeManager: TimeManager
) : ViewModel() {

    private val _buttonState: MutableLiveData<Boolean> = MutableLiveData()
    val timerRunning: LiveData<Boolean> = _buttonState

    val sessionName = repository.getSessionName()
    private var sessionTimerState = TimerState()

    val progress: LiveData<Float> = timeManager.progress
    val timer: LiveData<TimerState> = timeManager.timer


    init {
        viewModelScope.launch {
            repository.getFocusDuration().collect { duration ->
                sessionTimerState = TimerState(duration.toLong(), 0)
                timeManager.initiate(sessionTimerState)
            }
        }
    }

    fun startTimer() {
        _buttonState.value = true
        timeManager.startTimer(sessionTimerState)
    }

    fun pauseTimer() {
        _buttonState.value = false
        timeManager.pauseTimer()
    }

    fun restartTimer() {
        _buttonState.value = false
        timeManager.restartTimer(sessionTimerState)
    }
}