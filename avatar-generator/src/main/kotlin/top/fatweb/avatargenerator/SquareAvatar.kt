package top.fatweb.avatargenerator

import top.fatweb.avatargenerator.element.SquareElementRegistry
import java.awt.Color

/**
 * Util to generate Square style avatar
 *
 * @author FatttSnake, fatttsnake@gmail.com
 * @since 1.0.0
 */
object SquareAvatar {
    /**
     * Create new builder to generate Square style avatar
     *
     * @return avatar builder
     * @author FatttSnake, fatttsnake@gmail.com
     * @since 1.0.0
     * @see Avatar.AvatarBuilder
     */
    @JvmStatic
    fun newAvatarBuilder() = Avatar.newBuilder().elementRegistry(SquareElementRegistry())

    /**
     * Create new builder to generate Square style avatar
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
        Avatar.newBuilder().elementRegistry(SquareElementRegistry(3, colors.toList()))
}