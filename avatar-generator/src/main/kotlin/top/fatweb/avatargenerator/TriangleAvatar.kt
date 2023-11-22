package top.fatweb.avatargenerator

import top.fatweb.avatargenerator.element.TriangleElementRegistry
import java.awt.Color

/**
 * Util to generate Triangle style avatar
 *
 * @author FatttSnake, fatttsnake@gmail.com
 * @since 1.0.0
 */
object TriangleAvatar {
    /**
     * Create new builder to generate Triangle style avatar
     *
     * @return avatar builder
     * @author FatttSnake, fatttsnake@gmail.com
     * @since 1.0.0
     * @see Avatar.AvatarBuilder
     */
    @JvmStatic
    fun newAvatarBuilder() = Avatar.newBuilder().elementRegistry(TriangleElementRegistry())

    /**
     * Create new builder to generate Triangle style avatar
     *
     * @param colors
     * @return avatar builder
     * @author FatttSnake, fatttsnake@gmail.com
     * @since 1.0.0
     * @see Color
     * @see Avatar.AvatarBuilder
     */
    @JvmStatic
    fun newAvatarBuilder(vararg colors: Color) =
        Avatar.newBuilder().elementRegistry(TriangleElementRegistry(8, colors.toList()))
}