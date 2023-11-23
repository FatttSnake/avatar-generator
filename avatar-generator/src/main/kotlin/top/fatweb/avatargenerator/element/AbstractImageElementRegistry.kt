package top.fatweb.avatargenerator.element

import top.fatweb.avatargenerator.IAvatarInfo
import java.awt.Image
import java.awt.image.BufferedImage

/**
 * Abstract class for image element registry
 *
 * @author FatttSnake, fatttsnake@gmail.com
 * @since 1.0.0
 * @see ElementRegistry
 */
abstract class AbstractImageElementRegistry protected constructor() : ElementRegistry {
    /**
     * Get the count of elements
     * Always 1
     *
     * @param avatarInfo
     * @param name
     * @return count of elements
     * @author FatttSnake, fatttsnake@gmail.com
     * @since 1.0.0
     * @see IAvatarInfo
     */
    override fun getElementCount(avatarInfo: IAvatarInfo, name: String) = 1

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
    override fun getElement(avatarInfo: IAvatarInfo, name: String, index: Int) = buildImage(avatarInfo)

    /**
     * Get the count of groups
     *
     * @param avatarInfo
     * @return count of groups
     * @author FatttSnake, fatttsnake@gmail.com
     * @since 1.0.0
     * @see IAvatarInfo
     */
    override fun getGroupCount(avatarInfo: IAvatarInfo) = 1

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
    override fun getGroup(avatarInfo: IAvatarInfo, index: Int) = listOf(ElementInfo.of("github"))

    protected abstract fun buildImage(avatarInfo: IAvatarInfo): BufferedImage
}