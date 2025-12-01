package com.aay.compose.baseComponents

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import com.aay.compose.baseComponents.model.GridOrientation
import com.aay.compose.utils.formatToThousandsMillionsBillions

@OptIn(ExperimentalTextApi::class)
internal fun DrawScope.baseChartContainer(
    xAxisData: List<String>,
    textMeasure: TextMeasurer,
    upperValue: Float,
    lowerValue: Float,
    isShowGrid: Boolean,
    backgroundLineWidth: Float,
    gridColor: Color,
    showGridWithSpacer: Boolean,
    spacingY: Dp,
    yAxisStyle: TextStyle,
    xAxisStyle: TextStyle,
    yAxisRange: Int,
    showXAxis: Boolean,
    showYAxis: Boolean,
    specialChart: Boolean,
    isFromBarChart: Boolean,
    gridOrientation: GridOrientation,
    xRegionWidth: Dp
): Triple<Int, Float, Float> {

    val (actualSteps, niceMin, niceMax) = if (showYAxis) {
        yAxisDrawing(
            upperValue = upperValue,
            lowerValue = lowerValue,
            textMeasure = textMeasure,
            spacing = spacingY,
            yAxisStyle = yAxisStyle,
            yAxisRange = yAxisRange,
            specialChart = specialChart,
            isFromBarChart = isFromBarChart
        )
    } else {
        Triple(yAxisRange, lowerValue, upperValue)
    }

    if (showXAxis) {
        xAxisDrawing(
            xAxisData = xAxisData,
            textMeasure = textMeasure,
            xAxisStyle = xAxisStyle,
            specialChart = specialChart,
            upperValue = niceMax, // Use niceMax instead of upperValue
            xRegionWidth = xRegionWidth
        )
    }

    grid(
        xAxisDataSize = xAxisData.size,
        isShowGrid = isShowGrid,
        gridColor = gridColor,
        backgroundLineWidth = backgroundLineWidth,
        showGridWithSpacer = showGridWithSpacer,
        spacingY = spacingY,
        actualSteps = actualSteps,
        specialChart = specialChart,
        upperValue = niceMax,
        textMeasurer = textMeasure,
        gridOrientation = gridOrientation,
        xRegionWidth = xRegionWidth
    )

    return Triple(actualSteps, niceMin, niceMax)
}