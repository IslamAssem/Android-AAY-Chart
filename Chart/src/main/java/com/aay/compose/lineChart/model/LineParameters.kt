package com.aay.compose.lineChart.model

import androidx.compose.ui.graphics.Color
import com.aay.compose.points.PointDrawer

data class LineParameters(
    val label: String,
    val data: List<Double>,
    val lineColor: Color,
    val lineType: LineType,
    val lineShadow: Boolean,
    val pointDrawer: PointDrawer? = null,
){
    val upperValue = data.maxOrNull() ?: 0.0
    val lowerValue = data.minOrNull() ?: 0.0
}

fun List<LineParameters>.getUpperValue(): Double {
    var upperValue = 0.0
    this.forEach {
        if(it.upperValue > upperValue){
            upperValue = it.upperValue
        }
    }
    return upperValue
}
fun List<LineParameters>.getLowerValue(): Double {
    var lowerValue = Double.MAX_VALUE
    this.forEach {
        if(it.lowerValue < lowerValue){
            lowerValue = it.lowerValue
        }
    }
    return if(lowerValue == Double.MAX_VALUE) 0.0 else lowerValue
}
