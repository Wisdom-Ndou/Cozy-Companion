package com.example.ui.companion

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.data.model.CompanionType
import com.example.data.model.CustomItem
import com.example.data.model.ItemCategory

@Composable
fun CompanionView(
    companionType: CompanionType,
    moodState: CompanionMoodState,
    equippedItems: List<CustomItem>,
    bounceTrigger: Long,
    modifier: Modifier = Modifier,
    size: Dp = 190.dp,
) {
    // Gentle breathing loop
    val infiniteTransition = rememberInfiniteTransition(label = "breathing")
    val breathScale by infiniteTransition.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.025f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = if (moodState == CompanionMoodState.SLEEPING) 2400 else 1800,
                easing = FastOutSlowInEasing,
            ),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "breathScale",
    )

    val steamPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "steamPhase",
    )

    // Tap bounce animation
    val tapBounce by animateFloatAsState(
        targetValue = if (bounceTrigger > 0) 1f else 0f,
        animationSpec = spring(dampingRatio = 0.45f, stiffness = 400f),
        label = "tapBounce",
    )

    // Excitement jump / wiggle
    val exciteWiggle by infiniteTransition.animateFloat(
        initialValue = -3f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 250, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "exciteWiggle",
    )

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val canvasWidth = this.size.width
            val canvasHeight = this.size.height
            val centerX = canvasWidth / 2f
            val centerY = canvasHeight / 2f

            val jumpOffset = if ((moodState == CompanionMoodState.EXCITED) || (moodState == CompanionMoodState.CELEBRATING)) {
                tapBounce * -18f
            } else {
                tapBounce * -8f
            }

            val rotationAngle = if (moodState == CompanionMoodState.EXCITED) exciteWiggle else 0f

            rotate(degrees = rotationAngle, pivot = Offset(centerX, centerY)) {
                // Character base colors
                val (baseBodyColor, earInnerColor) = when (companionType) {
                    CompanionType.BEAR -> Color(0xFFD4A373) to Color(0xFFFAEDCD)
                    CompanionType.BUNNY -> Color(0xFFF7E1D7) to Color(0xFFF3C6C6)
                    CompanionType.CAT -> Color(0xFFE9D8A6) to Color(0xFFF4A261)
                }

                val bodyY = centerY + 10f + jumpOffset
                val scale = breathScale

                // 1. Equipped Rug / Shadow underneath
                drawOval(
                    color = Color(0x223D405B),
                    topLeft = Offset(centerX - (55f * scale), bodyY + 55f),
                    size = Size(110f * scale, 22f),
                )

                // 2. Ears
                drawCompanionEars(
                    companionType = companionType,
                    centerX = centerX,
                    headTopY = bodyY - 45f,
                    baseColor = baseBodyColor,
                    innerColor = earInnerColor,
                    mood = moodState,
                )

                // 3. Main Body & Head
                // Chibi round body
                drawOval(
                    color = baseBodyColor,
                    topLeft = Offset(centerX - (48f * scale), bodyY - 10f),
                    size = Size(96f * scale, 75f),
                )
                // Head
                drawCircle(
                    color = baseBodyColor,
                    radius = 42f * scale,
                    center = Offset(centerX, bodyY - 18f),
                )

                // 4. Equipped Clothing (Sweater, Hoodie, Pajamas)
                drawEquippedClothing(
                    equippedItems = equippedItems,
                    centerX = centerX,
                    bodyY = bodyY,
                    scale = scale,
                )

                // 5. Face Details
                drawCompanionFace(
                    mood = moodState,
                    centerX = centerX,
                    faceCenterY = bodyY - 18f,
                )

                // 6. Equipped Beanie / Glasses / Scarf
                drawEquippedAccessoriesTop(
                    equippedItems = equippedItems,
                    centerX = centerX,
                    faceCenterY = bodyY - 18f,
                    headTopY = bodyY - 55f,
                )

                // 7. Paws & Handheld Item (Mug, Plushie, Book)
                drawCompanionPawsAndHoldable(
                    equippedItems = equippedItems,
                    baseBodyColor = baseBodyColor,
                    centerX = centerX,
                    bodyY = bodyY,
                    mood = moodState,
                    steamPhase = steamPhase,
                )

                // 8. Sparkles / Zzz floating bubbles
                drawAmbientStatusFX(
                    mood = moodState,
                    centerX = centerX,
                    bodyY = bodyY,
                )
            }
        }
    }
}

private fun DrawScope.drawCompanionEars(
    companionType: CompanionType,
    centerX: Float,
    headTopY: Float,
    baseColor: Color,
    innerColor: Color,
    mood: CompanionMoodState,
) {
    when (companionType) {
        CompanionType.BEAR -> {
            // Left round ear
            drawCircle(color = baseColor, radius = 16f, center = Offset(centerX - 30f, headTopY + 5f))
            drawCircle(color = innerColor, radius = 9f, center = Offset(centerX - 30f, headTopY + 5f))
            // Right round ear
            drawCircle(color = baseColor, radius = 16f, center = Offset(centerX + 30f, headTopY + 5f))
            drawCircle(color = innerColor, radius = 9f, center = Offset(centerX + 30f, headTopY + 5f))
        }
        CompanionType.BUNNY -> {
            // Bunny long ears
            val earAngle = if (mood == CompanionMoodState.SLEEPING) 15f else 5f
            // Left ear
            rotate(-earAngle, pivot = Offset(centerX - 18f, headTopY + 10f)) {
                drawRoundRect(
                    color = baseColor,
                    topLeft = Offset(centerX - 26f, headTopY - 38f),
                    size = Size(16f, 48f),
                    cornerRadius = CornerRadius(8f, 8f)
                )
                drawRoundRect(
                    color = innerColor,
                    topLeft = Offset(centerX - 23f, headTopY - 32f),
                    size = Size(10f, 38f),
                    cornerRadius = CornerRadius(5f, 5f)
                )
            }
            // Right ear
            rotate(earAngle, pivot = Offset(centerX + 18f, headTopY + 10f)) {
                drawRoundRect(
                    color = baseColor,
                    topLeft = Offset(centerX + 10f, headTopY - 38f),
                    size = Size(16f, 48f),
                    cornerRadius = CornerRadius(8f, 8f)
                )
                drawRoundRect(
                    color = innerColor,
                    topLeft = Offset(centerX + 13f, headTopY - 32f),
                    size = Size(10f, 38f),
                    cornerRadius = CornerRadius(5f, 5f)
                )
            }
        }
        CompanionType.CAT -> {
            // Triangular cat ears
            val leftEarPath = Path().apply {
                moveTo(centerX - 38f, headTopY + 14f)
                lineTo(centerX - 24f, headTopY - 14f)
                lineTo(centerX - 10f, headTopY + 10f)
                close()
            }
            drawPath(leftEarPath, baseColor)
            val leftInnerPath = Path().apply {
                moveTo(centerX - 34f, headTopY + 12f)
                lineTo(centerX - 24f, headTopY - 7f)
                lineTo(centerX - 14f, headTopY + 9f)
                close()
            }
            drawPath(leftInnerPath, innerColor)

            val rightEarPath = Path().apply {
                moveTo(centerX + 10f, headTopY + 10f)
                lineTo(centerX + 24f, headTopY - 14f)
                lineTo(centerX + 38f, headTopY + 14f)
                close()
            }
            drawPath(rightEarPath, baseColor)
            val rightInnerPath = Path().apply {
                moveTo(centerX + 14f, headTopY + 9f)
                lineTo(centerX + 24f, headTopY - 7f)
                lineTo(centerX + 34f, headTopY + 12f)
                close()
            }
            drawPath(rightInnerPath, innerColor)
        }
    }
}

private fun DrawScope.drawCompanionFace(
    mood: CompanionMoodState,
    centerX: Float,
    faceCenterY: Float,
) {
    val eyeColor = Color(0xFF2B2D42)
    val blushColor = Color(0x66E07A5F)

    // Cheeks
    drawCircle(color = blushColor, radius = 9f, center = Offset(centerX - 24f, faceCenterY + 7f))
    drawCircle(color = blushColor, radius = 9f, center = Offset(centerX + 24f, faceCenterY + 7f))

    when (mood) {
        CompanionMoodState.SLEEPING -> {
            // Closed peaceful eyes
            val leftEyePath = Path().apply {
                moveTo(centerX - 22f, faceCenterY)
                quadraticTo(centerX - 15f, faceCenterY + 5f, centerX - 8f, faceCenterY)
            }
            drawPath(leftEyePath, eyeColor, style = Stroke(width = 2.8f, cap = StrokeCap.Round))

            val rightEyePath = Path().apply {
                moveTo(centerX + 8f, faceCenterY)
                quadraticTo(centerX + 15f, faceCenterY + 5f, centerX + 22f, faceCenterY)
            }
            drawPath(rightEyePath, eyeColor, style = Stroke(width = 2.8f, cap = StrokeCap.Round))

            // Gentle small mouth
            drawArc(
                color = eyeColor,
                startAngle = 20f,
                sweepAngle = 140f,
                useCenter = false,
                topLeft = Offset(centerX - 4f, faceCenterY + 7f),
                size = Size(8f, 5f),
                style = Stroke(width = 2f, cap = StrokeCap.Round)
            )
        }
        CompanionMoodState.WAKING -> {
            // Half-open drowsy eyes
            drawArc(
                color = eyeColor,
                startAngle = 190f,
                sweepAngle = 160f,
                useCenter = false,
                topLeft = Offset(centerX - 22f, faceCenterY - 3f),
                size = Size(14f, 8f),
                style = Stroke(width = 2.8f, cap = StrokeCap.Round)
            )
            drawArc(
                color = eyeColor,
                startAngle = 190f,
                sweepAngle = 160f,
                useCenter = false,
                topLeft = Offset(centerX + 8f, faceCenterY - 3f),
                size = Size(14f, 8f),
                style = Stroke(width = 2.8f, cap = StrokeCap.Round)
            )
            // Tiny sleepy yawn/smile
            drawOval(
                color = eyeColor,
                topLeft = Offset(centerX - 3.5f, faceCenterY + 7f),
                size = Size(7f, 6f)
            )
        }
        CompanionMoodState.CALM, CompanionMoodState.COMFORTING -> {
            // Gentle open round eyes with shine
            drawOval(color = eyeColor, topLeft = Offset(centerX - 20f, faceCenterY - 3f), size = Size(9f, 11f))
            drawCircle(color = Color.White, radius = 2.5f, center = Offset(centerX - 18f, faceCenterY))

            drawOval(color = eyeColor, topLeft = Offset(centerX + 11f, faceCenterY - 3f), size = Size(9f, 11f))
            drawCircle(color = Color.White, radius = 2.5f, center = Offset(centerX + 13f, faceCenterY))

            // Gentle smile
            drawArc(
                color = eyeColor,
                startAngle = 20f,
                sweepAngle = 140f,
                useCenter = false,
                topLeft = Offset(centerX - 5f, faceCenterY + 6f),
                size = Size(10f, 7f),
                style = Stroke(width = 2.2f, cap = StrokeCap.Round)
            )
        }
        CompanionMoodState.HAPPY, CompanionMoodState.EXCITED, CompanionMoodState.CELEBRATING, CompanionMoodState.PROUD -> {
            // Upward joyful crescent eyes ^ ^
            val leftEyePath = Path().apply {
                moveTo(centerX - 22f, faceCenterY + 3f)
                quadraticTo(centerX - 15f, faceCenterY - 5f, centerX - 8f, faceCenterY + 3f)
            }
            drawPath(leftEyePath, eyeColor, style = Stroke(width = 3.2f, cap = StrokeCap.Round))

            val rightEyePath = Path().apply {
                moveTo(centerX + 8f, faceCenterY + 3f)
                quadraticTo(centerX + 15f, faceCenterY - 5f, centerX + 22f, faceCenterY + 3f)
            }
            drawPath(rightEyePath, eyeColor, style = Stroke(width = 3.2f, cap = StrokeCap.Round))

            // Open happy smile
            val mouthPath = Path().apply {
                moveTo(centerX - 6f, faceCenterY + 7f)
                quadraticTo(centerX, faceCenterY + 15f, centerX + 6f, faceCenterY + 7f)
                close()
            }
            drawPath(mouthPath, Color(0xFFE07A5F))
            drawPath(mouthPath, eyeColor, style = Stroke(width = 2f))
        }
    }

    // Button nose
    drawCircle(color = Color(0xFFE07A5F), radius = 3.2f, center = Offset(centerX, faceCenterY + 3.5f))
}

private fun DrawScope.drawEquippedClothing(
    equippedItems: List<CustomItem>,
    centerX: Float,
    bodyY: Float,
    scale: Float
) {
    val equippedClothing = equippedItems.firstOrNull { (it.category == ItemCategory.CLOTHING) && it.isEquipped }
        ?: return

    when (equippedClothing.id) {
        "cloth_hoodie" -> {
            // Terracotta Hoodie
            drawRoundRect(
                color = Color(0xFFE07A5F),
                topLeft = Offset(centerX - (42f * scale), bodyY),
                size = Size(84f * scale, 58f),
                cornerRadius = CornerRadius(20f, 20f)
            )
            // Pocket & collar
            drawArc(
                color = Color(0xFFCC6A52),
                startAngle = 10f,
                sweepAngle = 160f,
                useCenter = false,
                topLeft = Offset(centerX - 16f, bodyY - 3f),
                size = Size(32f, 16f),
                style = Stroke(width = 4f, cap = StrokeCap.Round)
            )
            // Kangaroo pocket
            drawRoundRect(
                color = Color(0xFFCC6A52),
                topLeft = Offset(centerX - 24f, bodyY + 28f),
                size = Size(48f, 22f),
                cornerRadius = CornerRadius(8f, 8f)
            )
        }
        "cloth_sweater" -> {
            // Knit Cream Sweater
            drawRoundRect(
                color = Color(0xFFF4EDE4),
                topLeft = Offset(centerX - (42f * scale), bodyY),
                size = Size(84f * scale, 58f),
                cornerRadius = CornerRadius(20f, 20f)
            )
            // Cable knit lines
            for (i in -1..1) {
                drawLine(
                    color = Color(0xFFE3D5C8),
                    start = Offset(centerX + (i * 18f), bodyY + 6f),
                    end = Offset(centerX + (i * 18f), bodyY + 48f),
                    strokeWidth = 2.5f
                )
            }
        }
        "cloth_pajamas" -> {
            // Cloud pajamas
            drawRoundRect(
                color = Color(0xFFA8DADC),
                topLeft = Offset(centerX - (42f * scale), bodyY),
                size = Size(84f * scale, 58f),
                cornerRadius = CornerRadius(20f, 20f)
            )
            // Tiny star dots
            drawCircle(color = Color.White, radius = 2f, center = Offset(centerX - 18f, bodyY + 16f))
            drawCircle(color = Color.White, radius = 2f, center = Offset(centerX + 20f, bodyY + 22f))
            drawCircle(color = Color.White, radius = 2f, center = Offset(centerX - 6f, bodyY + 34f))
            drawCircle(color = Color.White, radius = 2f, center = Offset(centerX + 12f, bodyY + 42f))
        }
    }
}

private fun DrawScope.drawEquippedAccessoriesTop(
    equippedItems: List<CustomItem>,
    centerX: Float,
    faceCenterY: Float,
    headTopY: Float,
) {
    for (item in equippedItems) {
        if (!item.isEquipped) continue
        when (item.id) {
            "cloth_beanie" -> {
                // Knit Beanie over head
                drawRoundRect(
                    color = Color(0xFFF4A261),
                    topLeft = Offset(centerX - 36f, headTopY + 8f),
                    size = Size(72f, 28f),
                    cornerRadius = CornerRadius(16f, 16f)
                )
                // Pom-pom on top
                drawCircle(color = Color(0xFFFAEDCD), radius = 10f, center = Offset(centerX, headTopY + 4f))
            }
            "cloth_scarf" -> {
                // Warm amber scarf
                drawRoundRect(
                    color = Color(0xFFE76F51),
                    topLeft = Offset(centerX - 36f, faceCenterY + 16f),
                    size = Size(72f, 16f),
                    cornerRadius = CornerRadius(8f, 8f)
                )
                // Hanging tail of scarf
                drawRoundRect(
                    color = Color(0xFFE76F51),
                    topLeft = Offset(centerX + 14f, faceCenterY + 24f),
                    size = Size(14f, 28f),
                    cornerRadius = CornerRadius(4f, 4f)
                )
            }
            "cloth_glasses" -> {
                // Round spectacles
                val glassesColor = Color(0xFF6B705C)
                drawCircle(color = glassesColor, radius = 12f, center = Offset(centerX - 15f, faceCenterY), style = Stroke(width = 2.2f))
                drawCircle(color = glassesColor, radius = 12f, center = Offset(centerX + 15f, faceCenterY), style = Stroke(width = 2.2f))
                drawLine(
                    color = glassesColor,
                    start = Offset(centerX - 3f, faceCenterY - 1f),
                    end = Offset(centerX + 3f, faceCenterY - 1f),
                    strokeWidth = 2.2f
                )
            }
            "acc_headphones" -> {
                // Cozy over-ear headphones
                val headphoneColor = Color(0xFF264653)
                // Arc headband
                drawArc(
                    color = headphoneColor,
                    startAngle = 180f,
                    sweepAngle = 180f,
                    useCenter = false,
                    topLeft = Offset(centerX - 42f, headTopY + 4f),
                    size = Size(84f, 44f),
                    style = Stroke(width = 4f, cap = StrokeCap.Round)
                )
                // Left ear cup
                drawRoundRect(
                    color = headphoneColor,
                    topLeft = Offset(centerX - 46f, faceCenterY - 10f),
                    size = Size(12f, 22f),
                    cornerRadius = CornerRadius(6f, 6f)
                )
                // Right ear cup
                drawRoundRect(
                    color = headphoneColor,
                    topLeft = Offset(centerX + 34f, faceCenterY - 10f),
                    size = Size(12f, 22f),
                    cornerRadius = CornerRadius(6f, 6f)
                )
            }
        }
    }
}

private fun DrawScope.drawCompanionPawsAndHoldable(
    equippedItems: List<CustomItem>,
    baseBodyColor: Color,
    centerX: Float,
    bodyY: Float,
    mood: CompanionMoodState,
    steamPhase: Float,
) {
    val equippedAcc = equippedItems.firstOrNull { (it.category == ItemCategory.ACCESSORY) && it.isEquipped }

    // Check what is being held
    when (equippedAcc?.id) {
        "acc_mug" -> {
            // Steaming mug
            val mugX = centerX - 12f
            val mugY = bodyY + 16f
            // Mug body
            drawRoundRect(
                color = Color(0xFF81B29A),
                topLeft = Offset(mugX, mugY),
                size = Size(24f, 22f),
                cornerRadius = CornerRadius(4f, 4f)
            )
            // Mug handle
            drawArc(
                color = Color(0xFF81B29A),
                startAngle = 270f,
                sweepAngle = 180f,
                useCenter = false,
                topLeft = Offset(mugX + 18f, mugY + 3f),
                size = Size(10f, 14f),
                style = Stroke(width = 3f, cap = StrokeCap.Round)
            )
            // Rising warm steam squiggles
            val steamYOffset = -12f * steamPhase
            drawArc(
                color = Color(0x66FFFFFF),
                startAngle = 180f,
                sweepAngle = 180f,
                useCenter = false,
                topLeft = Offset(mugX + 4f, (mugY - 10f) + steamYOffset),
                size = Size(8f, 10f),
                style = Stroke(width = 1.8f, cap = StrokeCap.Round),
            )
            drawArc(
                color = Color(0x66FFFFFF),
                startAngle = 0f,
                sweepAngle = 180f,
                useCenter = false,
                topLeft = Offset(mugX + 12f, (mugY - 14f) + steamYOffset),
                size = Size(8f, 10f),
                style = Stroke(width = 1.8f, cap = StrokeCap.Round),
            )

            // Left and right paws clutching mug
            drawCircle(color = baseBodyColor, radius = 9f, center = Offset(centerX - 14f, bodyY + 24f))
            drawCircle(color = baseBodyColor, radius = 9f, center = Offset(centerX + 14f, bodyY + 24f))
        }
        "acc_plushie" -> {
            // Star plushie held in paws
            val starX = centerX
            val starY = bodyY + 24f
            drawCircle(color = Color(0xFFF4A261), radius = 12f, center = Offset(starX, starY))
            drawCircle(color = Color(0xFF2B2D42), radius = 1.5f, center = Offset(starX - 3f, starY - 1f))
            drawCircle(color = Color(0xFF2B2D42), radius = 1.5f, center = Offset(starX + 3f, starY - 1f))
            // Paws hugging plushie
            drawCircle(color = baseBodyColor, radius = 8f, center = Offset(centerX - 13f, bodyY + 26f))
            drawCircle(color = baseBodyColor, radius = 8f, center = Offset(centerX + 13f, bodyY + 26f))
        }
        "acc_book" -> {
            // Storybook
            drawRoundRect(
                color = Color(0xFF3D405B),
                topLeft = Offset(centerX - 16f, bodyY + 16f),
                size = Size(32f, 22f),
                cornerRadius = CornerRadius(3f, 3f)
            )
            drawRoundRect(
                color = Color(0xFFF7F5F0),
                topLeft = Offset(centerX - 14f, bodyY + 18f),
                size = Size(28f, 18f),
                cornerRadius = CornerRadius(2f, 2f)
            )
            // Paws holding book
            drawCircle(color = baseBodyColor, radius = 8f, center = Offset(centerX - 16f, bodyY + 26f))
            drawCircle(color = baseBodyColor, radius = 8f, center = Offset(centerX + 16f, bodyY + 26f))
        }
        else -> {
            // Default paws
            if ((mood == CompanionMoodState.EXCITED) || (mood == CompanionMoodState.CELEBRATING)) {
                // Waving paws up!
                drawCircle(color = baseBodyColor, radius = 10f, center = Offset(centerX - 36f, bodyY - 10f))
                drawCircle(color = baseBodyColor, radius = 10f, center = Offset(centerX + 36f, bodyY - 10f))
            } else {
                // Resting paws on belly
                drawCircle(color = baseBodyColor, radius = 9f, center = Offset(centerX - 22f, bodyY + 24f))
                drawCircle(color = baseBodyColor, radius = 9f, center = Offset(centerX + 22f, bodyY + 24f))
            }
        }
    }
}

private fun DrawScope.drawAmbientStatusFX(
    mood: CompanionMoodState,
    centerX: Float,
    bodyY: Float
) {
    when (mood) {
        CompanionMoodState.SLEEPING -> {
            // Little Zzz letters/bubbles
            drawCircle(color = Color(0x88F4A261), radius = 3.5f, center = Offset(centerX + 32f, bodyY - 40f))
            drawCircle(color = Color(0x88F4A261), radius = 5.5f, center = Offset(centerX + 42f, bodyY - 55f))
            drawCircle(color = Color(0x88F4A261), radius = 8f, center = Offset(centerX + 54f, bodyY - 72f))
        }
        CompanionMoodState.PROUD, CompanionMoodState.COMFORTING -> {
            // Floating gentle heart
            val heartPath = Path().apply {
                val hx = centerX + 34f
                val hy = bodyY - 48f
                moveTo(hx, hy)
                cubicTo(hx - 8f, hy - 8f, hx - 12f, hy + 4f, hx, hy + 12f)
                cubicTo(hx + 12f, hy + 4f, hx + 8f, hy - 8f, hx, hy)
                close()
            }
            drawPath(heartPath, Color(0xFFE07A5F))
        }
        CompanionMoodState.CELEBRATING, CompanionMoodState.EXCITED -> {
            // Floating sparkles / confetti
            val sparkleColor1 = Color(0xFFF4A261)
            val sparkleColor2 = Color(0xFF81B29A)
            val sparkleColor3 = Color(0xFFE07A5F)

            drawCircle(color = sparkleColor1, radius = 4f, center = Offset(centerX - 45f, bodyY - 50f))
            drawCircle(color = sparkleColor2, radius = 3.5f, center = Offset(centerX + 48f, bodyY - 45f))
            drawCircle(color = sparkleColor3, radius = 4.5f, center = Offset(centerX - 35f, bodyY - 75f))
            drawCircle(color = sparkleColor1, radius = 3f, center = Offset(centerX + 38f, bodyY - 78f))
            drawCircle(color = sparkleColor2, radius = 4f, center = Offset(centerX, bodyY - 72f))
        }
        else -> Unit
    }
}
