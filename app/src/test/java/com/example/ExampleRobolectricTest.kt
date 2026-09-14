package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.ui.companion.CompanionEvent
import com.example.ui.companion.CompanionMoodState
import com.example.ui.companion.CompanionStateMachine
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Cozy Companion", appName)
  }

  @Test
  fun `companion state machine transitions from sleeping to waking on tap`() {
    val testScope = TestScope()
    val stateMachine = CompanionStateMachine(testScope)

    assertEquals(CompanionMoodState.SLEEPING, stateMachine.uiState.value.moodState)

    stateMachine.handleEvent(CompanionEvent.Tap)
    assertEquals(CompanionMoodState.WAKING, stateMachine.uiState.value.moodState)

    stateMachine.handleEvent(CompanionEvent.ComfortRequested)
    assertEquals(CompanionMoodState.COMFORTING, stateMachine.uiState.value.moodState)
  }
}
