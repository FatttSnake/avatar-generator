package top.fatweb.avatargenerator.element

import top.fatweb.avatargenerator.IAvatarInfo
import java.awt.Image

/**
 * Public interface for element registry
 *
 * @author FatttSnake, fatttsnake@gmail.com
 * @since 1.0.0
 */
interface ElementRegistry {
    /**
     * Get the count of elements
     *
     * @param avatarInfo
     * @param name
     * @return count of elements
     * @author FatttSnake, fatttsnake@gmail.com
     * @since 1.0.0
     * @see IAvatarInfo
     */
    fun getElementCount(avatarInfo: IAvatarInfo, name: String): Int

    /**
     * Get the element in avatar info
     *
     * @param avatarInfo
     * @param name
     * @param index
     * @return element
     * @author FatttSnake, fatttsnake@gmail.com
     * @since 1.0.0
     * @see IAvatarInfo
     * @see Image
     */
    fun getElement(avatarInfo: IAvatarInfo, name: String, index: Int): Image

    /**
     * Get the count of groups
     *
     * @param avatarInfo
     * @return count of groups
     * @author FatttSnake, fatttsnake@gmail.com
     * @since 1.0.0
     * @see IAvatarInfo
     */
    fun getGroupCount(avatarInfo: IAvatarInfo): Int

    /**
     * Get the group in avatar info
     *
     * @param avatarInfo
     * @param index
     * @return element group
     * @author FatttSnake, fatttsnake@gmail.com
     * @since 1.0.0
     * @see IAvatarInfo
     * @see ElementInfo
     */
    fun getGroup(avatarInfo: IAvatarInfo, index: Int): List<ElementInfo>
}