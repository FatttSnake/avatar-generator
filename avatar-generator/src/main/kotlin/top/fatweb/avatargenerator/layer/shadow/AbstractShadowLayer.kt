package top.fatweb.avatargenerator.layer.shadow

import top.fatweb.avatargenerator.IAvatarInfo
import top.fatweb.avatargenerator.layer.ILayer
import top.fatweb.avatargenerator.util.AvatarUtil
import java.awt.image.BufferedImage

abstract class AbstractShadowLayer(private val first: Boolean) : ILayer {
    override fun apply(avatarInfo: IAvatarInfo, src: BufferedImage): BufferedImage {
        val width = src.width
        val height = src.height

        val dest = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        val graphics2D = dest.createGraphics()
        AvatarUtil.activeAntialiasing(graphics2D)
        if (first) {
            graphics2D.drawImage(buildShadow(avatarInfo, src), 0, 0, null)
        }
        graphics2D.drawImage(src, 0, 0, null)
        if (!first) {
            graphics2D.drawImage(buildShadow(avatarInfo, src), 0, 0, null)
        }
        graphics2D.dispose()

        return dest
    }

    protected abstract fun buildShadow(avatarInfo: IAvatarInfo, src: BufferedImage): BufferedImage
}