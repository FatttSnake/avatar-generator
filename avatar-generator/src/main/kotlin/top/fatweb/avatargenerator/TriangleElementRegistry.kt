package top.fatweb.avatargenerator

import com.jhlabs.image.ShadowFilter
import top.fatweb.avatargenerator.element.AbstractImageElementRegistry
import top.fatweb.avatargenerator.util.AvatarUtil
import java.awt.Color
import java.awt.Polygon
import java.awt.image.BufferedImage
import kotlin.math.max

class TriangleElementRegistry(
    private val precision: Int,
    private val colors: List<Color>
) : AbstractImageElementRegistry() {
    constructor() : this(8, AvatarUtil.defaultColors)

    override fun buildImage(avatarInfo: IAvatarInfo): BufferedImage {
        val size =
            (avatarInfo.getWidth() - (avatarInfo.getMargin() + avatarInfo.getPadding()) * 2).coerceAtMost(avatarInfo.getHeight() - (avatarInfo.getMargin() + avatarInfo.getPadding()) * 2)

        val dest = BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB)
        val graphics2D = dest.createGraphics()
        AvatarUtil.activeAntialiasing(graphics2D)

        val color = colors[avatarInfo.getRandom().nextInt(colors.size)]

        val ss = max(1.0, (size / 20f).toDouble()).toFloat()
        val shadowFilter = ShadowFilter(ss, 0f, 0f, 0.75f)

        val random = avatarInfo.getRandom()
        val s = random.nextInt(4)
        val n = s + precision
        var p = size
        val d = size / n
        var i = s
        while (p > 0) {
            var polygon: Polygon? = null
            when (i % 4) {
                0 -> polygon = Polygon(intArrayOf(0, p - 1, 0), intArrayOf(0, 0, p - 1), 3)
                1 -> polygon = Polygon(intArrayOf(size - 1, size - 1, size - p - 1), intArrayOf(0, p - 1, 0), 3)
                2 -> polygon = Polygon(
                    intArrayOf(size - 1, size - p - 1, size - 1),
                    intArrayOf(size - 1, size - 1, size - p - 1),
                    3
                )

                3 -> polygon = Polygon(intArrayOf(0, 0, p - 1), intArrayOf(size - 1, size - p - 1, size - 1), 3)
            }
            if (polygon != null) {
                graphics2D.drawImage(drawTriangle(size, color, polygon), shadowFilter, 0, 0)
            }
            p -= random.nextInt(d / 2) + d / 2
            i++
        }

        graphics2D.dispose()

        return AvatarUtil.planImage(dest, size - ss.toInt(), size - ss.toInt())
    }

    private fun drawTriangle(size: Int, color: Color, polygon: Polygon): BufferedImage {
        val dest = BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB)
        val graphics2D = dest.createGraphics()
        AvatarUtil.activeAntialiasing(graphics2D)
        graphics2D.color = color
        graphics2D.fill(polygon)
        graphics2D.dispose()

        return dest
    }
}