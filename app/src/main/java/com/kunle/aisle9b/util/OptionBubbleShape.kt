package com.kunle.aisle9b.util

import androidx.compose.foundation.shape.GenericShape
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection


class BubbleShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val trianglePath = Path().apply {
            moveTo(x = size.width / 2f, y = 0f)
            lineTo(x = size.width, y = size.height)
            lineTo(x = 0f, y = size.height)
        }
        return Outline.Generic(path = trianglePath)
    }

}

fun createBubbleShape(
    arrowWidth: Float,
    arrowHeight: Float,
    arrowOffset: Float,
): GenericShape {

    return GenericShape { size: Size, layoutDirection: LayoutDirection ->

        val width = size.width
        val height = size.height

        moveTo(arrowOffset, height - arrowHeight)
        lineTo(arrowOffset + arrowWidth / 2, height)
        lineTo(arrowOffset + arrowWidth, height - arrowHeight)

        addRoundRect(
            RoundRect(
                rect = Rect(
                    left = 0f,
                    top = 0f,
                    right = width,
                    bottom = height - arrowHeight
                ),
                cornerRadius = CornerRadius(x = 20f, y = 20f)
            )
        )
    }
}
