package com.example.ui.companion

enum class CompanionMoodState {
    SLEEPING,
    WAKING,
    CALM,
    HAPPY,
    EXCITED,
    CELEBRATING,
    COMFORTING,
    PROUD
}

@Suppress("unused")
data class CompanionMessage(
    val text: String,
    val state: CompanionMoodState,
)

object CompanionPhrases {
    val messages = mapOf(
        CompanionMoodState.SLEEPING to listOf(
            "Zzz... peaceful rest...",
            "Snug and dreaming...",
            "Gentle quiet moments...",
        ),
        CompanionMoodState.WAKING to listOf(
            "Mmm... you're here? I'm so happy to see you.",
            "*stretches paws* Hey friend, I'm right here.",
            "I woke up just for you. How are you feeling?",
        ),
        CompanionMoodState.CALM to listOf(
            "Just breathe with me.",
            "I'm right here.",
            "One moment at a time.",
            "Let's wait it out together.",
            "You don't have to rush anything.",
            "Stay with me for a bit.",
        ),
        CompanionMoodState.HAPPY to listOf(
            "You got this.",
            "You're stronger than this urge.",
            "I'm so glad you reached out.",
            "You don't have to give in.",
            "You're doing okay.",
            "We can get through this.",
        ),
        CompanionMoodState.EXCITED to listOf(
            "Look at you choosing yourself!",
            "Every single second you pause matters!",
            "I'm cheering for you so hard!",
            "You have so much strength in you!",
            "We're beating this urge step by step!",
        ),
        CompanionMoodState.CELEBRATING to listOf(
            "I'm so proud of you!",
            "You showed up for yourself today!",
            "Big or small, every victory is real!",
            "We did it! High five!",
        ),
        CompanionMoodState.COMFORTING to listOf(
            "It's okay. I'm still here.",
            "You're safe here, no judgment ever.",
            "Take all the time you need.",
            "One difficult moment doesn't erase your progress.",
            "We can always try again.",
            "I'm not going anywhere.",
        ),
        CompanionMoodState.PROUD to listOf(
            "You took care of yourself just now.",
            "That was an incredible pause you took.",
            "Your courage inspires me.",
            "Rest easy knowing you did something good for yourself.",
        ),
    )

    fun getRandomMessage(state: CompanionMoodState): String {
        val list = messages[state] ?: listOf("I'm here with you.")
        return list.random()
    }
}
