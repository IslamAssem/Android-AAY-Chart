package com.aay.compose.baseComponents

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.*
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.aay.compose.utils.formatToThousandsMillionsBillions
import kotlin.text.toFloat
import kotlin.times

@OptIn(ExperimentalTextApi::class)
internal fun <T> DrawScope.xAxisDrawing(
    xAxisData: List<T>,
    textMeasure: TextMeasurer,
    xAxisStyle: TextStyle,
    specialChart: Boolean,
    upperValue: Float,
    xRegionWidth: Dp
) {
    if (specialChart) return

    val yTextLayoutWidth = textMeasure.measure(
        AnnotatedString(upperValue.formatToThousandsMillionsBillions())
    ).size.width

    // Use the same calculation as in QuadraticLines.kt
    val textSpace = yTextLayoutWidth * 1.5f

    xAxisData.forEachIndexed { index, dataPoint ->
        val xLength = textSpace.toDp() + (index * xRegionWidth)

        drawContext.canvas.nativeCanvas.apply {
            val text = dataPoint.toString()

            // Measure label text
            val result = textMeasure.measure(AnnotatedString(text), style = xAxisStyle)
            val textHeight = result.size.height.toFloat()

            val xPos = xLength.coerceAtMost(size.width.toDp()).toPx()
            val yPos = size.height - 40.dp.toPx()

            save()

            // Rotate around the data point position
            translate(xPos, yPos)
            rotate(60f)
            // Adjust vertical position to center the text height after rotation
            translate(0f, -textHeight / 2f)

            // Draw text centered on the pivot
            drawText(
                textMeasurer = textMeasure,
                text = text,
                style = xAxisStyle,
                maxLines = 1,
                overflow = TextOverflow.Visible,
                topLeft = Offset.Zero
            )

            restore()
        }
    }
}
@OptIn(ExperimentalTextApi::class)
internal fun <T> DrawScope.xAxisDrawingOld(
    xAxisData: List<T>,
    textMeasure: TextMeasurer,
    xAxisStyle: TextStyle,
    specialChart: Boolean,
    upperValue: Float,
    xRegionWidth: Dp
) {
    if (specialChart) return

    val yTextLayoutWidth = textMeasure.measure(
        AnnotatedString(upperValue.formatToThousandsMillionsBillions())
    ).size.width

    val textSpace = yTextLayoutWidth - (yTextLayoutWidth / 4)

    xAxisData.forEachIndexed { index, dataPoint ->
        val xLength = textSpace.toDp() + (index * xRegionWidth)

        drawContext.canvas.nativeCanvas.apply {
            val text = dataPoint.toString()

            // Measure label text
            val result = textMeasure.measure(AnnotatedString(text), style = xAxisStyle)
            val textWidth = result.size.width.toFloat()
            val textHeight = result.size.height.toFloat()

            val xPos = xLength.coerceAtMost(size.width.toDp()).toPx()
            val yPos = size.height - 40.dp.toPx()

            save()

            // 🔥 Rotate around the text center
            translate(xPos, yPos)
            rotate(60f)  // rotation center =
            translate(0f, -10f)

            // Draw text centered on the pivot
            drawText(
                textMeasurer = textMeasure,
                text = text,
                style = xAxisStyle,
                maxLines = 1,
                overflow = TextOverflow.Visible,
                topLeft = Offset.Zero
            )

            restore()
        }
    }
}
