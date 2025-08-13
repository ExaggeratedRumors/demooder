package com.ertools.demooder.presentation.components.shapes

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.toPath

class PolygonShape(
    vertices: List<Float>,
    rounding: List<Float>
) : Shape {
    private val polygon: RoundedPolygon = RoundedPolygon(
        vertices = vertices.toFloatArray(),
        perVertexRounding = rounding.map { CornerRounding(it) }
    )
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val matrix = Matrix()
        val bounds = polygon.calculateBounds().let { Rect(it[0], it[1], it[2], it[3]) }
        matrix.scale(size.width / bounds.width, size.height / bounds.height)
        val path = polygon.toPath().asComposePath()
        path.transform(matrix)
        return Outline.Generic(path)
    }
}