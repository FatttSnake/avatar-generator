package top.fatweb.avatargenerator

import kotlin.random.Random

/**
 * Public interface for avatar info
 *
 * @author FatttSnake, fatttsnake@mail.com
 * @since 1.0.0
 */
interface IAvatarInfo {
    /**
     * Get seed
     *
     * @return seed
     * @author FatttSnake, fatttsnake@gmail.com
     * @since 1.0.0
     */
    fun getSeed(): Long

    /**
     * Get random
     *
     * @return random
     * @author FatttSnake, fatttsnake@gmail.com
     * @since 1.0.0
     * @see Random
     */
    fun getRandom(): Random

    /**
     * Get width
     *
     * @return width
     * @author FatttSnake, fatttsnake@gmail.com
     * @since 1.0.0
     */
    fun getWidth(): Int

    /**
     * Get height
     *
     * @return height
     * @author FatttSnake, fatttsnake@gmail.com
     * @since 1.0.0
     */
    fun getHeight(): Int

    /**
     * Get margin
     *
     * @return margin
     * @author FatttSnake, fatttsnake@gmail.com
     * @since 1.0.0
     */
    fun getMargin(): Int

    /**
     * Get padding
     *
     * @return padding
     * @author FatttSnake, fatttsnake@gmail.com
     * @since 1.0.0
     */
    fun getPadding(): Int
}