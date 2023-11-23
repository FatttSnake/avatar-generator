package top.fatweb.avatargenerator.element

import top.fatweb.avatargenerator.IAvatarInfo

/**
 * Abstract class for element registry
 *
 * @author FatttSnake, fatttsnake@gmail.com
 * @since 1.0.0
 * @see ElementRegistry
 */
abstract class AbstractElementRegistry : ElementRegistry {
    private val groups = mutableListOf<List<ElementInfo>>()

    /**
     * Put group
     *
     * @param elementInfo
     * @author FatttSnake, fatttsnake@gmail.com
     * @since 1.0.0
     * @see ElementInfo
     */
    fun putGroup(vararg elementInfo: ElementInfo) {
        groups.add(elementInfo.toList())
    }

    /**
     * Get the count of groups
     *
     * @param avatarInfo
     * @return count of groups
     * @author FatttSnake, fatttsnake@gmail.com
     * @since 1.0.0
     * @see IAvatarInfo
     */
    override fun getGroupCount(avatarInfo: IAvatarInfo) = groups.size

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
    override fun getGroup(avatarInfo: IAvatarInfo, index: Int) = groups[index]
}