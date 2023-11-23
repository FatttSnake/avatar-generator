package top.fatweb.avatargenerator.element

/**
 * Avatar info
 *
 * @author FatttSnake, fatttsnake@mail.com
 * @since 1.0.0
 */
class ElementInfo private constructor(
    val name: String,
    val offsetX: Int,
    val offsetY: Int
) {
    companion object {
        /**
         * Create element info
         *
         * @param name
         * @return element info object
         * @author FatttSnake, fatttsnake@gmail.com
         * @since 1.0.0
         * @see ElementInfo
         */
        @JvmStatic
        fun of(name: String) = ElementInfo(name, 0, 0)

        /**
         * Create element info
         *
         * @param name
         * @param offsetX
         * @param offsetY
         * @return element info object
         * @author FatttSnake, fatttsnake@gmail.com
         * @since 1.0.0
         * @see ElementInfo
         */
        @JvmStatic
        fun of(name: String, offsetX: Int, offsetY: Int) = ElementInfo(name, offsetX, offsetY)
    }
}
