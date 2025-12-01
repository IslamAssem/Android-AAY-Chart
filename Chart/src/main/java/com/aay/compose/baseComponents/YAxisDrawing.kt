package com.aay.compose.baseComponents

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.times
import com.aay.compose.utils.formatToThousandsMillionsBillions
import kotlin.div
import kotlin.jvm.kotlin
import kotlin.math.pow
import kotlin.text.toFloat
import kotlin.text.toInt
import kotlin.times

@OptIn(ExperimentalTextApi::class)
internal fun DrawScope.yAxisDrawing(
    upperValue: Float, lowerValue: Float,
    textMeasure: TextMeasurer,
    spacing: Dp,
    yAxisStyle: TextStyle,
    yAxisRange: Int,
    specialChart: Boolean,
    isFromBarChart: Boolean,
): Triple<Int, Float, Float> {
    if (specialChart) {
        return Triple(yAxisRange, lowerValue, upperValue)
    }
    val dataRange = if (isFromBarChart) upperValue else upperValue - lowerValue

    val roughStep = dataRange / yAxisRange
    val dataStep = getNiceNumber(roughStep)

    val niceMin = if (isFromBarChart) {
        0f
    } else {
        (kotlin.math.floor(lowerValue / dataStep) * dataStep).toFloat()
    }

    val niceMax = (kotlin.math.ceil(upperValue / dataStep) * dataStep).toFloat()
    val actualSteps = ((niceMax - niceMin) / dataStep).toInt()

    (0..actualSteps).forEach { i ->
        val yValue = niceMin + dataStep * i

        val y = (size.height.toDp() - spacing - i * (size.height.toDp() - spacing) / actualSteps)

        // Measure text to get its height for vertical centering
        val textLayoutResult = textMeasure.measure(
            text = yValue.formatToThousandsMillionsBillions(),
            style = yAxisStyle
        )
        val textHeight = textLayoutResult.size.height

        drawContext.canvas.nativeCanvas.apply {
            drawText(
                textMeasurer = textMeasure,
                text = yValue.formatToThousandsMillionsBillions(),
                style = yAxisStyle,
                topLeft = Offset(0f, y.toPx() - textHeight / 2f) // Center text vertically
            )
        }
    }

    return Triple(actualSteps, niceMin, niceMax)
}

@OptIn(ExperimentalTextApi::class)
internal fun DrawScope.yAxisDrawingOld(
    upperValue: Float, lowerValue: Float,
    textMeasure: TextMeasurer,
    spacing: Dp,
    yAxisStyle: TextStyle,
    yAxisRange: Int,
    specialChart: Boolean,
    isFromBarChart: Boolean,
): Triple<Int, Float, Float> {
    if (specialChart) {
        return Triple(yAxisRange, lowerValue, upperValue)
    }
    val dataRange = if (isFromBarChart) upperValue else upperValue - lowerValue

    val roughStep = dataRange / yAxisRange
    val dataStep = getNiceNumber(roughStep)

    val niceMin = if (isFromBarChart) {
        0f
    } else {
        (kotlin.math.floor(lowerValue / dataStep) * dataStep).toFloat()
    }

    val niceMax = (kotlin.math.ceil(upperValue / dataStep) * dataStep).toFloat()
    val actualSteps = ((niceMax - niceMin) / dataStep).toInt()

    (0..actualSteps).forEach { i ->
        val yValue = niceMin + dataStep * i

        val y = (size.height.toDp() - spacing - i * (size.height.toDp() - spacing) / actualSteps)
        drawContext.canvas.nativeCanvas.apply {
            drawText(
                textMeasurer = textMeasure,
                text = yValue.formatToThousandsMillionsBillions(),
                style = yAxisStyle,
                topLeft = Offset(0f, y.toPx())
            )
        }
    }

    return Triple(actualSteps, niceMin, niceMax)
}

private fun getNiceNumber(value: Float): Float {
    val exponent = kotlin.math.floor(kotlin.math.log10(value.toDouble()))
    val fraction = value / 10.0.pow(exponent)

    val niceFraction = when {
        fraction <= 1.0 -> 1.0
        fraction <= 2.0 -> 2.0
        fraction <= 5.0 -> 5.0
        else -> 10.0
    }

    return (niceFraction * 10.0.pow(exponent)).toFloat()
}