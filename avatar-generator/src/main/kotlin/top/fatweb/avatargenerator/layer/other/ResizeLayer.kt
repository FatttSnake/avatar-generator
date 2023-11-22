package top.fatweb.avatargenerator.layer.other

import top.fatweb.avatargenerator.IAvatarInfo
import top.fatweb.avatargenerator.layer.ILayer
import top.fatweb.avatargenerator.util.AvatarUtil
import java.awt.image.BufferedImage

/**
 * Resize layer
 *
 * @author FatttSnake, fatttsnake@gmail.com
 * @since 1.0.0
 * @see ILayer
 */
class ResizeLayer(var width: Int, var height: Int) : ILayer {
    override fun apply(avatarInfo: IAvatarInfo, src: BufferedImage) = AvatarUtil.resizeImage(src, width, height)
}