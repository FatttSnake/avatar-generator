package top.fatweb.avatargenerator.layer.shadow

import top.fatweb.avatargenerator.IAvatarInfo
import java.awt.Color
import java.awt.image.BufferedImage

class ScoreShadowLayer(var shadowColor: Color) : AbstractShadowLayer(false) {
    constructor() : this(Color(0, 0, 0, 24))

    override fun buildShadow(avatarInfo: IAvatarInfo, src: BufferedImage): BufferedImage {
        val width = src.width
        val height = src.height

        val colors = intArrayOf(shadowColor.red, shadowColor.green, shadowColor.blue, shadowColor.alpha)

        val dest = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        for (y in 0 until height / 2) {
            for (x in 0 until width) {
                dest.raster.setPixel(x, y, colors)
            }
        }

        return dest
    }
}