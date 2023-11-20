package top.fatweb.avatargenerator.layer.shadow

import top.fatweb.avatargenerator.IAvatarInfo
import java.awt.Color
import java.awt.image.BufferedImage

class LongShadowLayer(var shadowColor: Color) : AbstractShadowLayer(true) {
    constructor() : this(Color(0, 0, 0, 64))

    override fun buildShadow(avatarInfo: IAvatarInfo, src: BufferedImage): BufferedImage {
        val width = src.width
        val height = src.height
        val n = shadowColor.alpha.toDouble()
        val step = n / (width + height)

        val dest = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        for (y in 0 until height) {
            for (x in 0 until width) {
                if (isInShade(src, x, y)) {
                    val alpha = (n - (x + y) * step).toInt()
                    val colors = intArrayOf(shadowColor.red, shadowColor.green, shadowColor.blue, alpha)
                    dest.raster.setPixel(x, y, colors)
                }
            }
        }

        return dest
    }

    private fun isInShade(src: BufferedImage, x: Int, y: Int): Boolean {
        var tempX = x
        var tempY = y

        val colors = IntArray(4)
        while (true) {
            tempX -= 1
            tempY -= 1

            if (tempX < 0 || tempY < 0) {
                return false
            } else {
                src.raster.getPixel(tempX, tempY, colors)
                if (colors[3] > 0) {
                    return true
                }
            }
        }
    }
}