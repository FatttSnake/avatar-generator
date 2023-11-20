package top.fatweb.avatargenerator.layer.other

import top.fatweb.avatargenerator.IAvatarInfo
import top.fatweb.avatargenerator.layer.ILayer
import top.fatweb.avatargenerator.util.AvatarUtil
import java.awt.Color
import java.awt.image.BufferedImage

class RandomColorPaintLayer : ILayer {
    private val colors: List<Color>
    private val blackColor: Color
    private val whiteColor: Color

    constructor() : this(AvatarUtil.defaultColors, Color.BLACK, Color.WHITE)

    constructor(colors: List<Color>, blackColor: Color, whiteColor: Color) {
        this.colors = colors
        this.blackColor = blackColor
        this.whiteColor = whiteColor
    }

    override fun apply(avatarInfo: IAvatarInfo, src: BufferedImage): BufferedImage {
        val backColor = colors[(avatarInfo.getCode() % colors.size).toInt()]
        val foreColor = AvatarUtil.getComplementColor(backColor, blackColor, whiteColor)
        val width = src.width
        val height = src.height

        val dest = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        val graphics2D = dest.createGraphics()
        AvatarUtil.activeAntialiasing(graphics2D)
        graphics2D.paint = backColor
        graphics2D.fillRect(0, 0, width, height)
        graphics2D.drawImage(AvatarUtil.fillColorImage(src, foreColor), 0, 0, null)
        graphics2D.dispose()

        return dest
    }
}