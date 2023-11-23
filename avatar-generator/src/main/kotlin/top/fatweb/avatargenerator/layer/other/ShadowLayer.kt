package top.fatweb.avatargenerator.layer.other

import com.jhlabs.image.ShadowFilter
import top.fatweb.avatargenerator.IAvatarInfo
import top.fatweb.avatargenerator.layer.ILayer
import top.fatweb.avatargenerator.util.AvatarUtil
import java.awt.image.BufferedImage

/**
 * Shadow layer
 *
 * @author FatttSnake, fatttsnake@gmail.com
 * @since 1.0.0
 * @see ILayer
 */
class ShadowLayer : ILayer {
    private val size: Int
    private val shadowFilter: ShadowFilter

    constructor() : this(10, ShadowFilter(5f, 2.5f, -2.5f, 0.75f))

    constructor(size: Int, shadowFilter: ShadowFilter) {
        this.size = size
        this.shadowFilter = shadowFilter
    }

    override fun apply(avatarInfo: IAvatarInfo, src: BufferedImage): BufferedImage {
        val width = src.width + size
        val height = src.height + size

        val temp = AvatarUtil.planImage(src, width, height)

        val dest = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        val graphics2D = dest.createGraphics()
        AvatarUtil.activeAntialiasing(graphics2D)
        graphics2D.drawImage(temp, shadowFilter, 0, 0)
        graphics2D.dispose()

        return dest
    }
}