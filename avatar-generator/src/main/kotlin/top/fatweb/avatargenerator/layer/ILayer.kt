package top.fatweb.avatargenerator.layer

import top.fatweb.avatargenerator.IAvatarInfo
import java.awt.image.BufferedImage

interface ILayer {
    fun apply(avatarInfo: IAvatarInfo, src: BufferedImage): BufferedImage
}