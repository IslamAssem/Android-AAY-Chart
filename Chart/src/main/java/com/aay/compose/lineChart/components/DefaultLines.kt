package com.aay.compose.lineChart.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.aay.compose.lineChart.model.LineParameters
import com.aay.compose.utils.clickedOnThisPoint
import com.aay.compose.utils.formatToThousandsMillionsBillions

private var lastClickedPoint: Pair<Float, Float>? = null

@OptIn(ExperimentalTextApi::class)
internal fun DrawScope.drawDefaultLineWithShadow(
    line: LineParameters,
    lowerValue: Float,
    upperValue: Float,
    animatedProgress: Animatable<Float, AnimationVector1D>,
    spacingX: Dp,
    spacingY: Dp,
    clickedPoints: MutableList<Pair<Float, Float>>,
    textMeasure: TextMeasurer,
    xRegionWidth: Dp,
) {

    val strokePathOfDefaultLine = drawLineAsDefault(
        lineParameter = line,
        lowerValue = lowerValue,
        upperValue = upperValue,
        animatedProgress = animatedProgress,
        spacingY = spacingY,
        clickedPoints = clickedPoints,
        textMeasure = textMeasure,
        xRegionWidth = xRegionWidth
    )

    if (line.lineShadow) {
        val fillPath = strokePathOfDefaultLine.apply {
            lineTo(size.width - xRegionWidth.toPx() + 40.dp.toPx(), size.height * 40)
            lineTo(spacingX.toPx() * 2, size.height * 40)
            close()
        }
        clipRect(right = size.width * animatedProgress.value) {
            drawPath(
                path = fillPath, brush = Brush.verticalGradient(
                    colors = listOf(line.lineColor.copy(alpha = .3f), Color.Transparent),
                    endY = (size.height.toDp() - spacingY).toPx()
                )
            )
        }
    }

    drawCircles(
        lineParameter = line,
        lowerValue = lowerValue,
        upperValue = upperValue,
        spacingY = spacingY,
        textMeasure = textMeasure,
        xRegionWidth = xRegionWidth
    )
}

@OptIn(ExperimentalTextApi::class)
private fun DrawScope.drawLineAsDefault(
    lineParameter: LineParameters,
    lowerValue: Float,
    upperValue: Float,
    animatedProgress: Animatable<Float, AnimationVector1D>,
    spacingY: Dp,
    clickedPoints: MutableList<Pair<Float, Float>>,
    textMeasure: TextMeasurer,
    xRegionWidth: Dp
) = Path().apply {
    val height = size.height.toDp()
    drawPathLineWrapper(
        lineParameter = lineParameter,
        strokePath = this,
        animatedProgress = animatedProgress,
    ) { lineParameter, index ->

        val yTextLayoutResult = textMeasure.measure(
            text = AnnotatedString(upperValue.formatToThousandsMillionsBillions()),
        ).size.width

        val info = lineParameter.data[index]
        val ratio = (info - lowerValue) / (upperValue - lowerValue)
        val startXPoint = (yTextLayoutResult * 1.5.toFloat().toDp()) + (index * xRegionWidth)
        val startYPoint =
            (height.toPx() - spacingY.toPx() - (ratio * (height.toPx() - spacingY.toPx())))
        val tolerance = 20.dp.toPx()
        val savedClicks =
            clickedOnThisPoint(clickedPoints, startXPoint.toPx(), startYPoint, tolerance)


        if (savedClicks) {
            if (lastClickedPoint != null) {
                clickedPoints.clear()
                lastClickedPoint = null
            } else {
                lastClickedPoint = Pair(startXPoint.toPx(), startYPoint.toFloat())
                circleWithRectAndText(
                    x = startXPoint,
                    y = startYPoint,
                    textMeasure = textMeasure,
                    info = info,
                    stroke = Stroke(width = 2.dp.toPx()),
                    line = lineParameter,
                    animatedProgress = animatedProgress
                )
            }
        }

        if (index == 0) {
            moveTo(startXPoint.toPx(), startYPoint.toFloat())
        } else {
//            addOval(
//                Rect(
//                    left = startXPoint.toPx() - 2.dp,
//                    top = startYPoint - 2.dp,
//                    right = startXPoint.toPx() + 2.dp,.toPx()
//                    bottom = startYPoint + 2.dp.toPx()
//                )
//            )
            lineTo(startXPoint.toPx(), startYPoint.toFloat())
        }

        val radius = 4.dp.toPx()
//        drawCircle(
//            color = Color.Black,
//            radius = radius,
//            center = androidx.compose.ui.geometry.Offset(startXPoint.toPx(), startYPoint.toFloat()),
//            style = Stroke(width = 2.dp.toPx())
//        )
    }
}
@OptIn(ExperimentalTextApi::class)
fun DrawScope.drawCircles(
    lineParameter: LineParameters,
    lowerValue: Float,
    upperValue: Float,
    spacingY: Dp,
    textMeasure: TextMeasurer,
    xRegionWidth: Dp,
) {
    val height = size.height.toDp()

    lineParameter.data.forEachIndexed { index, info ->
        val yTextLayoutResult = textMeasure.measure(
            text = AnnotatedString(upperValue.formatToThousandsMillionsBillions()),
        ).size.width

        val ratio = (info - lowerValue) / (upperValue - lowerValue)

        // FIXED: Use same calculation as grid
        val xPoint = (yTextLayoutResult * 1.5.toFloat().toDp()) + index * xRegionWidth

        // FIXED: Remove the +11.dp.toPx() offset
        val yPoint = (height.toPx()
                - spacingY.toPx()
                - (ratio * (size.height.toDp() - spacingY).toPx())
                )
        lineParameter.pointDrawer?.drawPoint( this, androidx.compose.ui.geometry.Offset(xPoint.toPx(), yPoint.toFloat()))

//
//        lineParameter.pointDrawer?.drawPoint(
//            drawScope = this,
//            offset = Offset(xPoint.toPx(), yPoint),
//            lineParameter = lineParameter
//        )
    }
}

