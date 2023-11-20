package top.fatweb.avatargenerator.layer.background

import top.fatweb.avatargenerator.IAvatarInfo
import top.fatweb.avatargenerator.layer.ILayer
import top.fatweb.avatargenerator.util.AvatarUtil
import java.awt.Color
import java.awt.image.BufferedImage

class RandomColorPaintBackgroundLayer : ILayer {
    private val colors: List<Color>

    constructor() : this(AvatarUtil.defaultColors)

    constructor(colors: List<Color>) {
        this.colors = colors
    }

    override fun apply(avatarInfo: IAvatarInfo, src: BufferedImage): BufferedImage {
        val backColor = colors[(avatarInfo.getCode() % colors.size).toInt()]
        val width = src.width
        val height = src.height

        val dest = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        val graphics2D = dest.createGraphics()
        AvatarUtil.activeAntialiasing(graphics2D)
        graphics2D.paint = backColor
        graphics2D.fillRect(0, 0, width, height)
        graphics2D.drawImage(src, 0, 0, null)
        graphics2D.dispose()

        return dest
    }
}