package top.fatweb.avatargenerator.layer.mask

import top.fatweb.avatargenerator.IAvatarInfo
import top.fatweb.avatargenerator.layer.ILayer
import top.fatweb.avatargenerator.util.AvatarUtil
import java.awt.AlphaComposite
import java.awt.image.BufferedImage

/**
 * Abstract class for mask layer
 *
 * @author FatttSnake, fatttsnake@gmail.com
 * @since 1.0.0
 * @see ILayer
 */
abstract class AbstractMaskLayer : ILayer {
    override fun apply(avatarInfo: IAvatarInfo, src: BufferedImage): BufferedImage {
        val width = src.width
        val height = src.height

        val dest = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        val graphics2D = dest.createGraphics()
        AvatarUtil.activeAntialiasing(graphics2D)
        graphics2D.drawImage(src, 0, 0, null)
        val alphaComposite = AlphaComposite.getInstance(AlphaComposite.DST_IN, 1.0f)
        graphics2D.composite = alphaComposite
        graphics2D.drawImage(buildMask(avatarInfo, src), 0, 0, null)
        graphics2D.dispose()

        return dest
    }

    protected abstract fun buildMask(avatarInfo: IAvatarInfo, src: BufferedImage): BufferedImage
}