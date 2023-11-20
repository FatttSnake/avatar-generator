package top.fatweb.avatargenerator.layer.other

import top.fatweb.avatargenerator.IAvatarInfo
import top.fatweb.avatargenerator.layer.ILayer
import top.fatweb.avatargenerator.util.AvatarUtil
import java.awt.image.BufferedImage

class RatioLayer(var ratio: Double) : ILayer {
    override fun apply(avatarInfo: IAvatarInfo, src: BufferedImage): BufferedImage {
        var width = src.width
        var height = src.height

        if ((width / height).toDouble() == ratio) {
            return src
        }

        if (ratio >= 1) {
            if (width > height) {
                height = (width / ratio).toInt()
            } else {
                width = (height / ratio).toInt()
            }
        }

        return AvatarUtil.planImage(src, width, height)
    }
}