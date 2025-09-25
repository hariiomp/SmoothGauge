package com.example.animo

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlin.math.*

@Composable
fun SmoothGauge(
    modifier: Modifier = Modifier,
    minValue: Int = 0,
    maxValue: Int = 100,
    initialValue: Int = 0,
) {
    require(maxValue > minValue) { "maxValue must be greater than minValue" }

    val scope = rememberCoroutineScope()

    val animatedValue = remember {
        Animatable(initialValue.coerceIn(minValue, maxValue).toFloat())
    }
    val value by animatedValue.asState()

    var boxSize by remember { mutableStateOf(androidx.compose.ui.unit.IntSize.Zero) }

    Box(
        modifier = modifier
            .background(Color.Black, RoundedCornerShape(40.dp))
            .onSizeChanged { boxSize = it }
            .pointerInput(boxSize) {
                if (boxSize.width > 0 && boxSize.height > 0) {
                    detectDragGestures { change, _ ->
                        val pos = change.position
                        val centerX = boxSize.width / 2f
                        val centerY = boxSize.height / 2f
                        val angleRadians = atan2(pos.y - centerY, pos.x - centerX)
                        var angleDegrees = Math.toDegrees(angleRadians.toDouble()).toFloat()

                        angleDegrees = (angleDegrees + 180f).coerceIn(0f, 180f)

                        val newValue = minValue + (angleDegrees / 180f * (maxValue - minValue))

                        scope.launch {
                            animatedValue.animateTo(
                                newValue,
                                animationSpec = spring(
                                    dampingRatio = 0.7f,
                                    stiffness = 150f
                                )
                            )
                        }
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Text(
                text = value.roundToInt().toString(),
                fontSize = 48.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(32.dp)
            )


            Canvas(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .aspectRatio(1f)
            ) {
                val radius = size.width / 2.2f
                val center = Offset(size.width / 2, size.height / 2)

                val startAngle = -180f
                val sweepAngle = 180f
                val tickCount = 30

                for (i in 0 until tickCount) {
                    val angle = Math.toRadians(
                        (startAngle + (sweepAngle / (tickCount - 1)) * i).toDouble()
                    )

                    val progressFraction = i.toFloat() / (tickCount - 1)

                    val tickColor = if (progressFraction < 0.5f) {
                        Color(0xFF7EB8F8).copy(alpha = 0.85f)
                            .lerpTo(Color.White, progressFraction * 2)
                    } else {
                        Color.White.lerpTo(
                            Color(0xFFF8C37E),
                            (progressFraction - 0.5f) * 2)
                    }

                    val tickLength = 38f
                    val tickWidth = 7f

                    val start = Offset(
                        x = center.x + (radius - tickLength) * cos(angle).toFloat(),
                        y = center.y + (radius - tickLength) * sin(angle).toFloat()
                    )
                    val end = Offset(
                        x = center.x + radius * cos(angle).toFloat(),
                        y = center.y + radius * sin(angle).toFloat()
                    )

                    drawLine(
                        color = tickColor,
                        start = start,
                        end = end,
                        strokeWidth = tickWidth,
                        cap = StrokeCap.Round
                    )
                }

                val safeRange = (maxValue - minValue).takeIf { it > 0 } ?: 1
                val indicatorAngle = Math.toRadians(
                    (startAngle + (sweepAngle * (value - minValue) / safeRange)).toDouble()
                )

                val indicatorLength = radius + 20f
                val indicatorEnd = Offset(
                    x = center.x + indicatorLength * cos(indicatorAngle).toFloat(),
                    y = center.y + indicatorLength * sin(indicatorAngle).toFloat()
                )

                drawLine(
                    color = Color.Red,
                    start = center,
                    end = indicatorEnd,
                    strokeWidth = 18f,
                    cap = StrokeCap.Round
                )
            }
        }
    }
}


private fun Color.lerpTo(target: Color, fraction: Float): Color {
    val f = fraction.coerceIn(0f, 1f)
    return Color(
        red = lerp(this.red, target.red, f),
        green = lerp(this.green, target.green, f),
        blue = lerp(this.blue, target.blue, f),
        alpha = lerp(this.alpha, target.alpha, f)
    )
}

private fun lerp(start: Float, end: Float, fraction: Float): Float {
    return start * (1 - fraction) + end * fraction
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewSmoothGauge() {
    SmoothGauge(
        modifier = Modifier.fillMaxWidth(),
        initialValue = 50
    )
}
