package com.example.ui.companion

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.data.model.CompanionType
import com.example.data.model.CustomItem

@Composable
fun RoomView(
    companionType: CompanionType,
    moodState: CompanionMoodState,
    equippedItems: List<CustomItem>,
    bounceTrigger: Long,
    modifier: Modifier = Modifier,
) {
    val equippedIds = equippedItems.asSequence().filter { it.isEquipped }.map { it.id }.toSet()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(290.dp),
        contentAlignment = Alignment.Center,
    ) {
        // Background Room Canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val floorY = height * 0.72f

            // 1. Wall gradient (soft cozy cream to ivory)
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFFCF7F0), Color(0xFFF9F1E6)),
                    startY = 0f,
                    endY = floorY,
                ),
                topLeft = Offset(0f, 0f),
                size = Size(width, floorY),
            )

            // 2. Baseboard and floor
            drawRect(
                color = Color(0xFFDFCBB5),
                topLeft = Offset(0f, floorY - 6f),
                size = Size(width, 6f),
            )
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFEADCC9), Color(0xFFDFCEB7)),
                    startY = floorY,
                    endY = height,
                ),
                topLeft = Offset(0f, floorY),
                size = Size(width, height - floorY),
            )

            // Subtle floor planks lines
            drawLine(
                color = Color(0x33B89E82),
                start = Offset(0f, floorY + 28f),
                end = Offset(width, floorY + 28f),
                strokeWidth = 1.2f,
            )
            drawLine(
                color = Color(0x33B89E82),
                start = Offset(0f, floorY + 56f),
                end = Offset(width, floorY + 56f),
                strokeWidth = 1.2f,
            )

            // 3. Wall Decor: Sunlit Window
            if (equippedIds.contains("wall_window")) {
                drawRoomWindow(width = width)
            }

            // 4. Wall Decor: Poster
            if (equippedIds.contains("wall_poster")) {
                drawRoomPoster(width = width)
            }

            // 5. Lighting: Fairy lights
            if (equippedIds.contains("light_fairy")) {
                drawFairyLights(width = width)
            }

            // 6. Plant: Hanging Ivy
            if (equippedIds.contains("plant_ivy")) {
                drawHangingIvy(width = width)
            }

            // 7. Furniture: Bookshelf (left wall)
            if (equippedIds.contains("furn_bookshelf")) {
                drawMiniBookshelf(floorY = floorY)
            }

            // 8. Furniture: Armchair or Bed or Floor Cushion
            if (equippedIds.contains("furn_armchair")) {
                drawReadingArmchair(width = width, floorY = floorY)
            } else if (equippedIds.contains("furn_futon")) {
                drawFutonBed(width = width, floorY = floorY)
            } else if (equippedIds.contains("furn_cushion")) {
                drawFloorCushion(width = width, floorY = floorY)
            }

            // 9. Rugs on floor
            if (equippedIds.contains("rug_terracotta")) {
                drawTerracottaRug(width = width, floorY = floorY)
            } else if (equippedIds.contains("rug_cream")) {
                drawCreamRug(width = width, floorY = floorY)
            }

            // 10. Lighting: Desk Lamp (Right side)
            if (equippedIds.contains("light_lamp")) {
                drawDeskLamp(width = width, floorY = floorY)
            }

            // 11. Plants: Potted Monstera or Bonsai
            if (equippedIds.contains("plant_monstera")) {
                drawPottedMonstera(width = width, floorY = floorY)
            } else if (equippedIds.contains("plant_bonsai")) {
                drawPeaceBonsai(width = width, floorY = floorY)
            }
        }

        // Companion in the center of the room
        CompanionView(
            companionType = companionType,
            moodState = moodState,
            equippedItems = equippedItems,
            bounceTrigger = bounceTrigger,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp),
            size = 195.dp,
        )
    }
}

private fun DrawScope.drawRoomWindow(width: Float) {
    val winX = width * 0.18f
    val winY = 28f
    val winW = 68f
    val winH = 92f

    // Window frame exterior
    drawRoundRect(
        color = Color(0xFFC7A788),
        topLeft = Offset(winX, winY),
        size = Size(winW, winH),
        cornerRadius = CornerRadius(28f, 28f),
    )
    // Sky gradient inside window
    drawRoundRect(
        brush = Brush.verticalGradient(
            colors = listOf(Color(0xFFBDE0FE), Color(0xFFFFC8DD)),
            startY = winY,
            endY = winY + winH,
        ),
        topLeft = Offset(winX + 4f, winY + 4f),
        size = Size(winW - 8f, winH - 8f),
        cornerRadius = CornerRadius(24f, 24f),
    )
    // Cloud inside window
    drawCircle(color = Color(0xCCFFFFFF), radius = 10f, center = Offset(winX + 26f, winY + 38f))
    drawCircle(color = Color(0xCCFFFFFF), radius = 14f, center = Offset(winX + 38f, winY + 36f))
    drawCircle(color = Color(0xCCFFFFFF), radius = 11f, center = Offset(winX + 48f, winY + 40f))

    // Wooden window pane divider
    drawLine(
        color = Color(0xFFC7A788),
        start = Offset(winX + (winW / 2f), winY + 4f),
        end = Offset(winX + (winW / 2f), winY + (winH - 4f)),
        strokeWidth = 3.5f,
    )
    drawLine(
        color = Color(0xFFC7A788),
        start = Offset(winX + 4f, winY + (winH * 0.52f)),
        end = Offset(winX + (winW - 4f), winY + (winH * 0.52f)),
        strokeWidth = 3.5f,
    )
}

private fun DrawScope.drawRoomPoster(width: Float) {
    val px = width * 0.76f
    val py = 32f
    val pw = 52f
    val ph = 70f

    // Frame
    drawRoundRect(
        color = Color(0xFF6B705C),
        topLeft = Offset(px, py),
        size = Size(pw, ph),
        cornerRadius = CornerRadius(4f, 4f),
    )
    // Poster inner art
    drawRoundRect(
        color = Color(0xFFFDF8F0),
        topLeft = Offset(px + 3f, py + 3f),
        size = Size(pw - 6f, ph - 6f),
        cornerRadius = CornerRadius(2f, 2f),
    )
    // Minimalist sun & mountain
    drawCircle(color = Color(0xFFF4A261), radius = 8f, center = Offset(px + (pw / 2f), py + 24f))
    val mountainPath = Path().apply {
        moveTo(px + 8f, py + (ph - 8f))
        lineTo(px + (pw / 2f), py + 36f)
        lineTo(px + (pw - 8f), py + (ph - 8f))
        close()
    }
    drawPath(mountainPath, Color(0xFF81B29A))
}

private fun DrawScope.drawFairyLights(width: Float) {
    // Hanging curved cord
    val cordPath = Path().apply {
        moveTo(10f, 14f)
        quadraticTo(width * 0.25f, 32f, width * 0.5f, 18f)
        quadraticTo(width * 0.75f, 34f, width - 10f, 16f)
    }
    drawPath(cordPath, Color(0xFF7F7F7F), style = Stroke(width = 1.5f))

    // Warm golden glowing bulbs
    val bulbPositions = listOf(
        Offset(width * 0.12f, 24f),
        Offset(width * 0.25f, 30f),
        Offset(width * 0.38f, 23f),
        Offset(width * 0.5f, 18f),
        Offset(width * 0.63f, 26f),
        Offset(width * 0.75f, 32f),
        Offset(width * 0.88f, 23f),
    )
    for (pos in bulbPositions) {
        // Soft aura glow
        drawCircle(color = Color(0x44FFE3A8), radius = 10f, center = pos)
        drawCircle(color = Color(0xFFFFD166), radius = 4f, center = pos)
    }
}

private fun DrawScope.drawHangingIvy(width: Float) {
    val vineColor = Color(0xFF4A7C59)
    val leafColor = Color(0xFF6B9080)

    val vinePath = Path().apply {
        moveTo(width - 4f, 0f)
        cubicTo(width - 15f, 25f, width - 35f, 40f, width - 28f, 75f)
    }
    drawPath(vinePath, vineColor, style = Stroke(width = 2.5f, cap = StrokeCap.Round))

    // Leaves along vine
    val leaves = listOf(
        Offset(width - 10f, 18f),
        Offset(width - 24f, 32f),
        Offset(width - 18f, 46f),
        Offset(width - 32f, 60f),
        Offset(width - 26f, 72f),
    )
    for (leaf in leaves) {
        drawOval(color = leafColor, topLeft = Offset(leaf.x - 5f, leaf.y - 4f), size = Size(11f, 8f))
    }
}

private fun DrawScope.drawMiniBookshelf(floorY: Float) {
    val bx = 16f
    val by = floorY - 95f
    val bw = 46f
    val bh = 95f

    // Shelf wood
    drawRoundRect(
        color = Color(0xFFB5835A),
        topLeft = Offset(bx, by),
        size = Size(bw, bh),
        cornerRadius = CornerRadius(4f, 4f),
    )
    // Shelf dividers
    drawRect(color = Color(0xFF996B45), topLeft = Offset(bx + 4f, by + 46f), size = Size(bw - 8f, 5f))

    // Books on top shelf
    drawRoundRect(color = Color(0xFFE07A5F), topLeft = Offset(bx + 6f, by + 10f), size = Size(8f, 34f), cornerRadius = CornerRadius(2f, 2f))
    drawRoundRect(color = Color(0xFF81B29A), topLeft = Offset(bx + 15f, by + 14f), size = Size(7f, 30f), cornerRadius = CornerRadius(2f, 2f))
    drawRoundRect(color = Color(0xFFF4A261), topLeft = Offset(bx + 23f, by + 8f), size = Size(9f, 36f), cornerRadius = CornerRadius(2f, 2f))

    // Bottom shelf books & plant
    drawRoundRect(color = Color(0xFF3D405B), topLeft = Offset(bx + 6f, by + 56f), size = Size(8f, 34f), cornerRadius = CornerRadius(2f, 2f))
    drawRoundRect(color = Color(0xFFE76F51), topLeft = Offset(bx + 15f, by + 60f), size = Size(7f, 30f), cornerRadius = CornerRadius(2f, 2f))
    // Tiny succulent pot
    drawRoundRect(color = Color(0xFFD4A373), topLeft = Offset(bx + 26f, by + 68f), size = Size(13f, 18f), cornerRadius = CornerRadius(2f, 2f))
    drawCircle(color = Color(0xFF588157), radius = 6f, center = Offset(bx + 32.5f, by + 65f))
}

private fun DrawScope.drawReadingArmchair(width: Float, floorY: Float) {
    val ax = width * 0.14f
    val ay = floorY - 58f

    // Cozy mustard armchair
    val chairColor = Color(0xFFE9C46A)
    val chairShadow = Color(0xFFD4A373)

    // Backrest
    drawRoundRect(
        color = chairColor,
        topLeft = Offset(ax, ay),
        size = Size(54f, 52f),
        cornerRadius = CornerRadius(14f, 14f),
    )
    // Seat cushion
    drawRoundRect(
        color = chairShadow,
        topLeft = Offset(ax - 4f, ay + 32f),
        size = Size(62f, 24f),
        cornerRadius = CornerRadius(8f, 8f),
    )
}

private fun DrawScope.drawFutonBed(width: Float, floorY: Float) {
    val fx = width * 0.12f
    val fy = floorY - 34f

    // Warm futon mattress
    drawRoundRect(
        color = Color(0xFFDDA15E),
        topLeft = Offset(fx, fy),
        size = Size(76f, 32f),
        cornerRadius = CornerRadius(6f, 6f),
    )
    // Fluffy quilt
    drawRoundRect(
        color = Color(0xFFCCD5AE),
        topLeft = Offset(fx + 18f, fy + 4f),
        size = Size(56f, 26f),
        cornerRadius = CornerRadius(5f, 5f),
    )
    // Pillow
    drawRoundRect(
        color = Color(0xFFFEFAE0),
        topLeft = Offset(fx + 4f, fy + 4f),
        size = Size(20f, 18f),
        cornerRadius = CornerRadius(4f, 4f),
    )
}

private fun DrawScope.drawFloorCushion(width: Float, floorY: Float) {
    val cx = width * 0.22f
    val cy = floorY - 18f
    drawOval(color = Color(0xFFE07A5F), topLeft = Offset(cx, cy), size = Size(52f, 22f))
    drawOval(color = Color(0xFFF4A261), topLeft = Offset(cx + 4f, cy + 2f), size = Size(44f, 16f))
}

private fun DrawScope.drawCreamRug(width: Float, floorY: Float) {
    val centerX = width / 2f
    drawOval(
        color = Color(0xFFF4EDE4),
        topLeft = Offset(centerX - 92f, floorY + 4f),
        size = Size(184f, 46f)
    )
    drawOval(
        color = Color(0xFFE8DDD2),
        topLeft = Offset(centerX - 82f, floorY + 8f),
        size = Size(164f, 38f)
    )
}

private fun DrawScope.drawTerracottaRug(width: Float, floorY: Float) {
    val centerX = width / 2f
    drawOval(
        color = Color(0xFFE07A5F),
        topLeft = Offset(centerX - 96f, floorY + 4f),
        size = Size(192f, 48f)
    )
    // Pattern stripes
    drawOval(
        color = Color(0xFFFAEDCD),
        topLeft = Offset(centerX - 84f, floorY + 8f),
        size = Size(168f, 38f)
    )
    drawOval(
        color = Color(0xFFD4A373),
        topLeft = Offset(centerX - 72f, floorY + 12f),
        size = Size(144f, 28f)
    )
}

private fun DrawScope.drawDeskLamp(width: Float, floorY: Float) {
    val lx = width * 0.82f
    val ly = floorY - 64f

    // Soft warm ambient circular glow
    drawCircle(
        color = Color(0x33FFD166),
        radius = 42f,
        center = Offset(lx + 10f, ly + 14f)
    )

    // Base & stand
    drawLine(
        color = Color(0xFF5A4E3E),
        start = Offset(lx + 10f, ly + 22f),
        end = Offset(lx + 10f, floorY),
        strokeWidth = 3f
    )
    drawOval(
        color = Color(0xFF5A4E3E),
        topLeft = Offset(lx, floorY - 5f),
        size = Size(20f, 8f)
    )

    // Warm lampshade
    val shadePath = Path().apply {
        moveTo(lx + 2f, ly + 22f)
        lineTo(lx + 18f, ly + 22f)
        lineTo(lx + 14f, ly + 6f)
        lineTo(lx + 6f, ly + 6f)
        close()
    }
    drawPath(shadePath, Color(0xFFF4A261))
}

private fun DrawScope.drawPottedMonstera(width: Float, floorY: Float) {
    val px = width * 0.78f
    val py = floorY - 52f

    // Leaves
    val leafColor = Color(0xFF386641)
    drawOval(color = leafColor, topLeft = Offset(px - 14f, py), size = Size(22f, 32f))
    drawOval(color = leafColor, topLeft = Offset(px + 4f, py - 8f), size = Size(24f, 34f))
    drawOval(color = leafColor, topLeft = Offset(px - 6f, py + 8f), size = Size(18f, 26f))

    // Terracotta pot
    val potPath = Path().apply {
        moveTo(px - 8f, floorY - 20f)
        lineTo(px + 18f, floorY - 20f)
        lineTo(px + 15f, floorY)
        lineTo(px - 5f, floorY)
        close()
    }
    drawPath(potPath, Color(0xFFBC6C25))
}

private fun DrawScope.drawPeaceBonsai(width: Float, floorY: Float) {
    val px = width * 0.78f
    val py = floorY - 38f

    // Gnarled trunk
    val trunkPath = Path().apply {
        moveTo(px + 4f, floorY - 10f)
        quadraticTo(px + 14f, py + 18f, px + 8f, py + 8f)
    }
    drawPath(trunkPath, Color(0xFF6B4D2B), style = Stroke(width = 4.5f, cap = StrokeCap.Round))

    // Cloud clusters of leaves
    drawCircle(color = Color(0xFF2D6A4F), radius = 12f, center = Offset(px + 8f, py + 8f))
    drawCircle(color = Color(0xFF40916C), radius = 10f, center = Offset(px + 18f, py + 12f))

    // Wide flat shallow pot
    drawRoundRect(
        color = Color(0xFF344E41),
        topLeft = Offset(px - 6f, floorY - 10f),
        size = Size(28f, 10f),
        cornerRadius = CornerRadius(2f, 2f)
    )
}
