package com.aay.compose.utils

import androidx.compose.ui.unit.Dp
import com.aay.compose.lineChart.model.LineParameters

internal fun checkIfDataValid(
    xAxisData: List<String>,
    linesParameters: List<LineParameters> = emptyList(),
) {

        val data = linesParameters.map { it.data }
        data.forEach {
            if (it.size != xAxisData.size) {
                throw Exception("The data size of line must be equal to the x-axis data size.")
            }
            checkIfDataIsNegative(it)
    }
}

internal fun checkIfDataIsNegative(data: List<Double>) {
    data.forEach {
        if (it < 0.0) {
            throw Exception("The data can't contains negative values.")
        }
    }
}