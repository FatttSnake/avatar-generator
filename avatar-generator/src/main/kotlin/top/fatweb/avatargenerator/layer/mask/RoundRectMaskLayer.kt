package top.fatweb.avatargenerator.layer.mask

import top.fatweb.avatargenerator.IAvatarInfo
import top.fatweb.avatargenerator.util.AvatarUtil
import java.awt.Color
import java.awt.image.BufferedImage

/**
 * Round rect mask layer
 *
 * @author FatttSnake, fatttsnake@gmail.com
 * @since 1.0.0
 * @see AbstractMaskLayer
 */
class RoundRectMaskLayer : AbstractMaskLayer {
    private val percent: Double

    constructor() : this(0.5)

    constructor(percent: Double) {
        this.percent = percent
    }

    override fun buildMask(avatarInfo: IAvatarInfo, src: BufferedImage): BufferedImage {
        val width = src.width
        val height = src.height

        val mask = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        val graphics2D = mask.createGraphics()
        AvatarUtil.activeAntialiasing(graphics2D)
        graphics2D.color = Color.white
        graphics2D.fillRoundRect(0, 0, width, height, (width * percent).toInt(), (height * percent).toInt())
        graphics2D.dispose()

        return mask
    }
}