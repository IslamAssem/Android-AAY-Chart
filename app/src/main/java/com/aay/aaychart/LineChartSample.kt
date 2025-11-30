package com.aay.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aay.compose.baseComponents.model.GridOrientation
import com.aay.compose.baseComponents.model.LegendPosition
import com.aay.compose.lineChart.LineChart
import com.aay.compose.lineChart.model.LineParameters
import com.aay.compose.lineChart.model.LineType
import com.aay.compose.points.PointDrawer

@Composable
fun LineChartSample() {

    val pointDrawer = object : PointDrawer{
        override fun drawPoint(
            drawScope: DrawScope,
            center: Offset
        ) {

            drawScope.drawCircle(
                color = Color.Black,
                radius = 15f,
                center = center,
                style = Stroke(width = 6f)
            )
        }
    }
    val pointDrawer2 = object : PointDrawer{
        override fun drawPoint(
            drawScope: DrawScope,
            center: Offset
        ) {

            drawScope.drawCircle(
                color = Color.Red,
                radius = 15f,
                center = center,
                style = Stroke(width = 6f)
            )
        }
    }
    val testLineParameters: List<LineParameters> = listOf(
//        LineParameters(
//            label = "revenue",
//            data = listOf(7000000.0, 00.0, 50000000.33, 40000000.0, 100000000.500, 50000000.0),
//            lineColor = Color.Gray,
//            lineType = LineType.CURVED_LINE,
//            lineShadow = true,
//        ),
        LineParameters(
            label = "Nov 25",
            data = listOf(1.0, 1.0),
            lineColor = Color(0xFFFF7F50),
            lineType = LineType.DEFAULT_LINE,
            lineShadow = true,
            pointDrawer = FilledCircularPointDrawer(color = Color(0xFFFF7F50))
        ),

        LineParameters(
            label = "Dec 25",
        data = listOf(73.0, 50.0),
            lineColor = Color(0xFF81BE88),
            lineType = LineType.CURVED_LINE,
            lineShadow = false,
            pointDrawer = pointDrawer2//FilledCircularPointDrawer(color = Color(0xFF81BE88))
        )
    )

    Column(Modifier.padding(top = 16.dp, start = 16.dp, bottom = 16.dp, end = 16.dp)) {
        MyBarChartParent(modifier =  Modifier.height(300.dp).fillMaxSize().padding(horizontal = 20.dp),listOf("Nov 25", "Dec 25"),testLineParameters)
        LineChart(
            modifier = Modifier.height(300.dp).fillMaxSize().padding(horizontal = 20.dp),
            linesParameters = testLineParameters,
            isGrid = true,
            gridColor = Color.Gray,
            xAxisData = listOf("Nov 25", "Dec 25"),
            animateChart = true,
            showGridWithSpacer = false,
            yAxisStyle = TextStyle(
                fontSize = 14.sp,
                color = Color.Gray,
            ),
            xAxisStyle = TextStyle(
                fontSize = 14.sp,
                color = Color.Gray,
                fontWeight = FontWeight.W400
            ),
            yAxisRange = 14,
            oneLineChart = false,
            gridOrientation = GridOrientation.GRID,
            legendPosition = LegendPosition.TOP
        )
    }
}

data class FilledCircularPointDrawer(
    val diameter: Dp = 10.dp,
    val color: Color = Color.Blue
) : PointDrawer {

    private val paint = Paint().apply {
        color = this@FilledCircularPointDrawer.color
        style = PaintingStyle.Fill
        isAntiAlias = true
    }

    override fun drawPoint(drawScope: DrawScope, center: Offset) {

        with(drawScope as Density) {
            drawScope.drawCircle( color = color, radius = diameter.toPx() / 2f, center = center )
            0
        }
    }
}

@Composable
fun MyBarChartParent(
    modifier: Modifier = Modifier,
    labels: List<String>,
    lineParameters: List<LineParameters>
) {
            if (labels.size > 0) {
                LineChart(
                    modifier = modifier,
                    linesParameters = lineParameters,
                    isGrid = true,
                    gridColor = Color.LightGray,
                    xAxisData = labels,
                    animateChart = true,
                    showYAxis = true,
                    showXAxis = true,
                    showGridWithSpacer = false,
                    yAxisStyle = TextStyle(
                        fontSize = 14.sp,
                        color = Color.Gray,
                    ),
                    xAxisStyle = TextStyle(
                        fontSize = 14.sp,
                        color = Color.Gray,
                    ),
                    descriptionStyle = TextStyle(
                        fontSize = 14.sp,
                        color = Color.Gray,
                    ),
                    yAxisRange = 14,
                    oneLineChart = false,
                    gridOrientation = GridOrientation.GRID,
                    legendPosition = LegendPosition.BOTTOM,
                )
    }
}
