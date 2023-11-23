package top.fatweb.avatargenerator.layer.background

import top.fatweb.avatargenerator.IAvatarInfo
import java.awt.Color
import java.awt.Paint
import java.awt.RadialGradientPaint
import java.awt.image.BufferedImage
import kotlin.math.sqrt

/**
 * Radial paint background layer
 *
 * @author FatttSnake, fatttsnake@gmail.com
 * @since 1.0.0
 * @see AbstractPaintBackgroundLayer
 */
class RadialPaintBackgroundLayer : AbstractPaintBackgroundLayer {
    private val inColor: Color
    private val outColor: Color

    constructor() : this(Color(0xE2A6FF), Color(0xC58BFF))

    constructor(inColor: Color, outColor: Color) {
        this.inColor = inColor
        this.outColor = outColor
    }

    override fun buildPaint(avatarInfo: IAvatarInfo, bufferedImage: BufferedImage): Paint {
        val width = bufferedImage.width
        val height = bufferedImage.height

        return RadialGradientPaint(
            (width / 2).toFloat(),
            (height / 2).toFloat(),
            (sqrt((width * width + height * height).toDouble()) / 2).toInt().toFloat(),
            floatArrayOf(0.0f, 0.75f),
            arrayOf(inColor, outColor)
        )
    }
}