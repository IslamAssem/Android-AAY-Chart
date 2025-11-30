package com.aay.compose.points
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.drawscope.DrawScope

interface PointDrawer {
  fun drawPoint(
    drawScope: DrawScope,
    center: Offset
  )
}