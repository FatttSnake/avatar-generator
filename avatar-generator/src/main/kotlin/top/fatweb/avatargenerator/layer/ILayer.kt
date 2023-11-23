package top.fatweb.avatargenerator.layer

import top.fatweb.avatargenerator.IAvatarInfo
import java.awt.image.BufferedImage

/**
 * Public interface for layer
 *
 * @author FatttSnake, fatttsnake@mail.com
 * @since 1.0.0
 */
interface ILayer {
    /**
     * Apply transform with src image
     *
     * @param avatarInfo
     * @param src
     * @return image
     * @author FatttSnake, fatttsnake@gmail.com
     * @since 1.0.0
     */
    fun apply(avatarInfo: IAvatarInfo, src: BufferedImage): BufferedImage
}