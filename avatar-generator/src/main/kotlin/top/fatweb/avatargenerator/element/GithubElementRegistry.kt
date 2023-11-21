package top.fatweb.avatargenerator.element

import top.fatweb.avatargenerator.IAvatarInfo
import top.fatweb.avatargenerator.util.AvatarUtil
import java.awt.image.BufferedImage

class GithubElementRegistry(private val size: Int, private val precision: Int) : AbstractImageElementRegistry() {
    constructor() : this(400, 5)

    override fun buildImage(avatarInfo: IAvatarInfo): BufferedImage {
        val dest = BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB)
        val graphics2D = dest.createGraphics()
        AvatarUtil.activeAntialiasing(graphics2D)
        graphics2D.color = AvatarUtil.extractColor(avatarInfo.getCode())

        val multi = size / (precision * 2 - 1)
        for (x in 0 until precision) {
            for (y in 0 until precision * 2) {
                if (avatarInfo.getRandom().nextDouble() < 0.5) {
                    graphics2D.fillRect(x * multi, y * multi, multi, multi)
                    graphics2D.fillRect(size - (x + 1) * multi, y * multi, multi, multi)
                }
            }
        }

        graphics2D.dispose()

        return dest
    }
}