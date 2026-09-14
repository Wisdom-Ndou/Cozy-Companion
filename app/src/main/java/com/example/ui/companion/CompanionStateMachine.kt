package com.example.ui.companion

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

sealed class CompanionEvent {
    object Tap : CompanionEvent()
    object ComfortRequested : CompanionEvent()
    object ExerciseCompleted : CompanionEvent()
    object RelapseReflectionStarted : CompanionEvent()
    object IdleTimeout : CompanionEvent()
    object PutToSleep : CompanionEvent()
}

data class CompanionUiState(
    val moodState: CompanionMoodState = CompanionMoodState.SLEEPING,
    val currentMessage: String = CompanionPhrases.getRandomMessage(CompanionMoodState.SLEEPING),
    val tapEnergy: Int = 0, // 0 to 100
    val sessionTaps: Int = 0,
    val bounceTrigger: Long = 0L,
)

class CompanionStateMachine(
    private val scope: CoroutineScope,
    private val onPointsEarned: (Int) -> Unit = {},
    private val onUrgeRelieved: () -> Unit = {},
) {
    private val _uiState = MutableStateFlow(CompanionUiState())
    val uiState: StateFlow<CompanionUiState> = _uiState.asStateFlow()

    private var idleJob: Job? = null

    fun handleEvent(event: CompanionEvent) {
        when (event) {
            is CompanionEvent.Tap -> onUserTap()
            is CompanionEvent.ComfortRequested -> setMood(CompanionMoodState.COMFORTING)
            is CompanionEvent.ExerciseCompleted -> {
                setMood(CompanionMoodState.PROUD)
                resetIdleTimer(durationMs = 20000L)
            }
            is CompanionEvent.RelapseReflectionStarted -> setMood(CompanionMoodState.COMFORTING)
            is CompanionEvent.IdleTimeout -> handleIdleWindDown()
            is CompanionEvent.PutToSleep -> setMood(CompanionMoodState.SLEEPING)
        }
    }

    private fun onUserTap() {
        val current = _uiState.value
        val newSessionTaps = current.sessionTaps + 1
        val newEnergy = (current.tapEnergy + 15).coerceAtMost(100)

        val nextMood = when (current.moodState) {
            CompanionMoodState.SLEEPING -> CompanionMoodState.WAKING
            CompanionMoodState.WAKING -> CompanionMoodState.CALM
            CompanionMoodState.CALM -> {
                if (newSessionTaps >= 3) CompanionMoodState.HAPPY else CompanionMoodState.CALM
            }
            CompanionMoodState.HAPPY -> {
                if (newSessionTaps >= 6) CompanionMoodState.EXCITED else CompanionMoodState.HAPPY
            }
            CompanionMoodState.EXCITED -> {
                if (newSessionTaps >= 10) CompanionMoodState.CELEBRATING else CompanionMoodState.EXCITED
            }
            CompanionMoodState.CELEBRATING -> CompanionMoodState.CELEBRATING
            CompanionMoodState.COMFORTING -> {
                if (newSessionTaps >= 4) CompanionMoodState.CALM else CompanionMoodState.COMFORTING
            }
            CompanionMoodState.PROUD -> CompanionMoodState.HAPPY
        }

        // Award points for every 5 taps during interaction (emotional distraction during urge)
        if ((newSessionTaps % 5) == 0) {
            onPointsEarned(5)
        }
        if (newSessionTaps == 10) {
            onUrgeRelieved()
        }

        _uiState.value = current.copy(
            moodState = nextMood,
            currentMessage = CompanionPhrases.getRandomMessage(nextMood),
            tapEnergy = newEnergy,
            sessionTaps = newSessionTaps,
            bounceTrigger = System.currentTimeMillis(),
        )

        resetIdleTimer()
    }

    private fun setMood(mood: CompanionMoodState) {
        _uiState.value = _uiState.value.copy(
            moodState = mood,
            currentMessage = CompanionPhrases.getRandomMessage(mood),
            bounceTrigger = System.currentTimeMillis(),
        )
    }

    private fun resetIdleTimer(durationMs: Long = 18000L) {
        idleJob?.cancel()
        idleJob = scope.launch {
            delay(durationMs.milliseconds)
            handleEvent(CompanionEvent.IdleTimeout)
        }
    }

    private fun handleIdleWindDown() {
        val current = _uiState.value
        val nextMood = when (current.moodState) {
            CompanionMoodState.CELEBRATING -> CompanionMoodState.EXCITED
            CompanionMoodState.EXCITED -> CompanionMoodState.HAPPY
            CompanionMoodState.HAPPY -> CompanionMoodState.CALM
            CompanionMoodState.CALM, CompanionMoodState.WAKING, CompanionMoodState.PROUD, CompanionMoodState.COMFORTING -> CompanionMoodState.SLEEPING
            CompanionMoodState.SLEEPING -> CompanionMoodState.SLEEPING
        }

        val newEnergy = (current.tapEnergy - 30).coerceAtLeast(0)
        _uiState.value = current.copy(
            moodState = nextMood,
            currentMessage = CompanionPhrases.getRandomMessage(nextMood),
            tapEnergy = newEnergy,
            sessionTaps = if (nextMood == CompanionMoodState.SLEEPING) 0 else current.sessionTaps,
        )

        if (nextMood != CompanionMoodState.SLEEPING) {
            resetIdleTimer(durationMs = 12000L)
        }
    }
}
