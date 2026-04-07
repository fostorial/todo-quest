package com.thexm.todoquest.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.thexm.todoquest.data.model.PatternType
import com.thexm.todoquest.data.model.ProfileBackground
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@Composable
fun ProfileBackgroundCanvas(
    background: ProfileBackground,
    modifier: Modifier = Modifier
) {
    val baseColors = background.gradientColors.map { Color(it) }
    val patternColor = Color.White.copy(alpha = background.patternAlpha)

    Box(modifier = modifier) {
        // Base gradient layer
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(
                brush = Brush.linearGradient(
                    colors = baseColors,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, size.height)
                )
            )
        }
        // Pattern overlay layer
        if (background.patternType != PatternType.NONE) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                when (background.patternType) {
                    PatternType.DIAGONAL_LINES       -> drawDiagonalLines(patternColor, spacing = 24f, strokeWidth = 1.5f)
                    PatternType.DIAGONAL_LINES_DENSE -> drawDiagonalLines(patternColor, spacing = 12f, strokeWidth = 1f)
                    PatternType.GRID                 -> drawGrid(patternColor, spacing = 28f)
                    PatternType.DOTS                 -> drawDots(patternColor, spacing = 22f, radius = 2f)
                    PatternType.LARGE_DOTS           -> drawDots(patternColor, spacing = 36f, radius = 4f)
                    PatternType.DIAMONDS             -> drawDiamonds(patternColor, size = 20f)
                    PatternType.SCALES               -> drawScales(patternColor, size = 28f)
                    PatternType.CHEVRONS             -> drawChevrons(patternColor, spacing = 24f)
                    PatternType.STARS                -> drawStars(patternColor, spacing = 48f)
                    PatternType.HEXAGONS             -> drawHexagons(patternColor, radius = 18f)
                    PatternType.WAVES                -> drawWaves(patternColor, spacing = 20f)
                    PatternType.BRICKS               -> drawBricks(patternColor, brickW = 40f, brickH = 18f)
                    PatternType.CROSSHATCH           -> drawCrosshatch(patternColor, spacing = 18f)
                    PatternType.TRIANGLES            -> drawTriangles(patternColor, size = 28f)
                    PatternType.NONE                 -> {}
                }
            }
        }
    }
}

// ── Pattern draw functions ────────────────────────────────────────────────────

private fun DrawScope.drawDiagonalLines(color: Color, spacing: Float, strokeWidth: Float) {
    val stroke = Stroke(width = strokeWidth)
    val total = size.width + size.height
    var offset = -size.height
    while (offset < size.width) {
        drawLine(
            color = color,
            start = Offset(offset, 0f),
            end = Offset(offset + size.height, size.height),
            strokeWidth = strokeWidth
        )
        offset += spacing
    }
}

private fun DrawScope.drawGrid(color: Color, spacing: Float) {
    val stroke = Stroke(width = 1f)
    var x = 0f
    while (x <= size.width) {
        drawLine(color, Offset(x, 0f), Offset(x, size.height), 1f)
        x += spacing
    }
    var y = 0f
    while (y <= size.height) {
        drawLine(color, Offset(0f, y), Offset(size.width, y), 1f)
        y += spacing
    }
}

private fun DrawScope.drawDots(color: Color, spacing: Float, radius: Float) {
    var y = spacing / 2f
    while (y < size.height) {
        var x = spacing / 2f
        while (x < size.width) {
            drawCircle(color, radius = radius, center = Offset(x, y))
            x += spacing
        }
        y += spacing
    }
}

private fun DrawScope.drawDiamonds(color: Color, size: Float) {
    val stroke = Stroke(width = 1.2f)
    var row = 0
    var y = size / 2f
    while (y < this.size.height + size) {
        val offsetX = if (row % 2 == 0) 0f else size
        var x = offsetX
        while (x < this.size.width + size) {
            val path = Path().apply {
                moveTo(x, y - size / 2f)
                lineTo(x + size / 2f, y)
                lineTo(x, y + size / 2f)
                lineTo(x - size / 2f, y)
                close()
            }
            drawPath(path, color, style = stroke)
            x += size * 2f
        }
        y += size
        row++
    }
}

private fun DrawScope.drawScales(color: Color, size: Float) {
    val stroke = Stroke(width = 1.5f)
    val stepY = size * 0.65f
    var row = 0
    var y = 0f
    while (y < this.size.height + size) {
        val offsetX = if (row % 2 == 0) 0f else size
        var x = offsetX - size / 2f
        while (x < this.size.width + size) {
            drawArc(
                color = color,
                startAngle = 0f,
                sweepAngle = 180f,
                useCenter = false,
                topLeft = Offset(x - size / 2f, y - size / 2f),
                size = Size(size, size),
                style = stroke
            )
            x += size
        }
        y += stepY
        row++
    }
}

private fun DrawScope.drawChevrons(color: Color, spacing: Float) {
    val stroke = Stroke(width = 1.5f)
    val halfW = spacing * 0.8f
    var y = spacing / 2f
    while (y < this.size.height + spacing) {
        var x = -halfW
        while (x < this.size.width + halfW) {
            val path = Path().apply {
                moveTo(x, y + spacing / 3f)
                lineTo(x + halfW, y - spacing / 3f)
                lineTo(x + halfW * 2f, y + spacing / 3f)
            }
            drawPath(path, color, style = stroke)
            x += halfW * 2f
        }
        y += spacing
    }
}

private fun DrawScope.drawStars(color: Color, spacing: Float) {
    val stroke = Stroke(width = 1.2f)
    var row = 0
    var y = spacing / 2f
    while (y < this.size.height + spacing) {
        val offsetX = if (row % 2 == 0) 0f else spacing / 2f
        var x = offsetX + spacing / 2f
        while (x < this.size.width + spacing) {
            drawStar(color, Offset(x, y), outerR = spacing * 0.18f, innerR = spacing * 0.08f, points = 5, strokeWidth = 1.2f)
            x += spacing
        }
        y += spacing * 0.9f
        row++
    }
}

private fun DrawScope.drawStar(
    color: Color,
    center: Offset,
    outerR: Float,
    innerR: Float,
    points: Int,
    strokeWidth: Float
) {
    val path = Path()
    val angleStep = Math.PI / points
    for (i in 0 until points * 2) {
        val r = if (i % 2 == 0) outerR else innerR
        val angle = i * angleStep - Math.PI / 2.0
        val x = center.x + (r * cos(angle)).toFloat()
        val y = center.y + (r * sin(angle)).toFloat()
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()
    drawPath(path, color, style = Stroke(width = strokeWidth))
}

private fun DrawScope.drawHexagons(color: Color, radius: Float) {
    val stroke = Stroke(width = 1.2f)
    val hexW = radius * sqrt(3f)
    val hexH = radius * 2f
    val colStep = hexW
    val rowStep = hexH * 0.75f
    var row = 0
    var y = 0f
    while (y < this.size.height + hexH) {
        val offsetX = if (row % 2 == 0) 0f else colStep / 2f
        var x = offsetX
        while (x < this.size.width + hexW) {
            drawHexagon(color, Offset(x, y), radius, stroke)
            x += colStep
        }
        y += rowStep
        row++
    }
}

private fun DrawScope.drawHexagon(color: Color, center: Offset, radius: Float, stroke: Stroke) {
    val path = Path()
    for (i in 0..5) {
        val angle = Math.PI / 180.0 * (60 * i - 30)
        val x = center.x + (radius * cos(angle)).toFloat()
        val y = center.y + (radius * sin(angle)).toFloat()
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()
    drawPath(path, color, style = stroke)
}

private fun DrawScope.drawWaves(color: Color, spacing: Float) {
    val stroke = Stroke(width = 1.5f)
    val amplitude = spacing * 0.4f
    val waveLength = spacing * 2.5f
    var y = spacing / 2f
    while (y < this.size.height + spacing) {
        val path = Path()
        var x = 0f
        path.moveTo(x, y)
        while (x < this.size.width + waveLength) {
            path.cubicTo(
                x + waveLength / 4f, y - amplitude,
                x + waveLength * 3f / 4f, y + amplitude,
                x + waveLength, y
            )
            x += waveLength
        }
        drawPath(path, color, style = stroke)
        y += spacing
    }
}

private fun DrawScope.drawBricks(color: Color, brickW: Float, brickH: Float) {
    val stroke = Stroke(width = 1f)
    var row = 0
    var y = 0f
    while (y < this.size.height + brickH) {
        val offsetX = if (row % 2 == 0) 0f else brickW / 2f
        var x = offsetX - brickW
        while (x < this.size.width + brickW) {
            drawRect(
                color = color,
                topLeft = Offset(x, y),
                size = Size(brickW - 2f, brickH - 2f),
                style = stroke
            )
            x += brickW
        }
        y += brickH
        row++
    }
}

private fun DrawScope.drawCrosshatch(color: Color, spacing: Float) {
    drawDiagonalLines(color, spacing, 1f)
    // Opposite diagonal
    var offset = -this.size.height
    while (offset < this.size.width) {
        drawLine(
            color = color,
            start = Offset(offset + this.size.height, 0f),
            end = Offset(offset, this.size.height),
            strokeWidth = 1f
        )
        offset += spacing
    }
}

private fun DrawScope.drawTriangles(color: Color, size: Float) {
    val stroke = Stroke(width = 1.2f)
    val h = size * sqrt(3f) / 2f
    var row = 0
    var y = 0f
    while (y < this.size.height + h) {
        var col = 0
        var x = 0f
        while (x < this.size.width + size) {
            val flipped = (row + col) % 2 == 1
            val path = Path()
            if (!flipped) {
                path.moveTo(x, y + h)
                path.lineTo(x + size / 2f, y)
                path.lineTo(x + size, y + h)
            } else {
                path.moveTo(x, y)
                path.lineTo(x + size / 2f, y + h)
                path.lineTo(x + size, y)
            }
            path.close()
            drawPath(path, color, style = stroke)
            x += size
            col++
        }
        y += h
        row++
    }
}
