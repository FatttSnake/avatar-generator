package top.fatweb.avatargenerator.element

import top.fatweb.avatargenerator.IAvatarInfo
import top.fatweb.avatargenerator.util.AvatarUtil
import java.awt.Color
import java.awt.image.BufferedImage

/**
 * Element registry for Square style avatar
 *
 * @author FatttSnake, fatttsnake@gmail.com
 * @since 1.0.0
 * @see AbstractImageElementRegistry
 */
class SquareElementRegistry(
    private val precision: Int,
    private val colors: List<Color>
) : AbstractImageElementRegistry() {
    constructor() : this(3, AvatarUtil.defaultColors)

    override fun buildImage(avatarInfo: IAvatarInfo): BufferedImage {
        val size = (avatarInfo.getWidth() - (avatarInfo.getMargin() + avatarInfo.getPadding()) * 2).coerceAtMost(
            avatarInfo.getHeight() - (avatarInfo.getMargin() + avatarInfo.getPadding()) * 2
        ) * precision
        val d = size / (precision * 5)

        val dest = BufferedImage(size + d * 2, size + d * 2, BufferedImage.TYPE_INT_ARGB)
        val graphics2D = dest.createGraphics()
        AvatarUtil.activeAntialiasing(graphics2D)

        val index = avatarInfo.getRandom().nextInt(colors.size)

        val fillColor = colors[index]
        val backgroundColor = colors[(index + 1) % colors.size]

        graphics2D.color = fillColor
        graphics2D.fillRect(0, 0, size + d * 2, size + d * 2)

        graphics2D.color = backgroundColor
        graphics2D.fillRect(d, d, size, size)

        graphics2D.color = fillColor
        val multi = size / precision
        for (x in 0 until precision) {
            for (y in 0 until precision) {
                if (avatarInfo.getRandom().nextDouble() < 0.5) {
                    graphics2D.fillRect(x * multi + d, y * multi + d, multi, multi)
                }
            }
        }

        graphics2D.dispose()

        return dest
    }
}