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
import kotlin.math.pow


@OptIn(ExperimentalTextApi::class)
internal fun DrawScope.yAxisDrawing(
    upperValue: Float, lowerValue: Float,
    textMeasure: TextMeasurer,
    spacing: Dp,
    yAxisStyle: TextStyle,
    yAxisRange: Int,
    specialChart: Boolean,
    isFromBarChart: Boolean,
): Int { // Return the actual steps used
    if (specialChart) {
        return yAxisRange
    }
    val dataRange = if (isFromBarChart) upperValue else upperValue - lowerValue

    // Calculate a "nice" step value
    val roughStep = dataRange / yAxisRange
    val dataStep = getNiceNumber(roughStep)

    // Calculate nice min and max values
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

    return actualSteps // Return the actual number of steps used
}

// Helper function to get "nice" numbers for axis labels
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