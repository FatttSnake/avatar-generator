package top.fatweb.avatargenerator

import top.fatweb.avatargenerator.element.IdenticonElementRegistry
import top.fatweb.avatargenerator.element.identicon.NineBlockIdenticonRenderer

/**
 * Util to generate Identicon style avatar
 *
 * @author FatttSnake, fatttsnake@gmail.com
 * @since 1.0.0
 */
object IdenticonAvatar {
    /**
     * Create new builder to generate Identicon style avatar
     *
     * @return avatar builder
     * @author FatttSnake, fatttsnake@gmail.com
     * @since 1.0.0
     * @see Avatar.AvatarBuilder
     */
    @JvmStatic
    fun newAvatarBuilder() = Avatar.newBuilder().elementRegistry(IdenticonElementRegistry())

    /**
     * Create new builder to generate Identicon style avatar
     *
     * @param nineBlockIdenticonRenderer
     * @return avatar builder
     * @author FatttSnake, fatttsnake@gmail.com
     * @since 1.0.0
     * @see NineBlockIdenticonRenderer
     * @see Avatar.AvatarBuilder
     */
    @JvmStatic
    fun newAvatarBuilder(nineBlockIdenticonRenderer: NineBlockIdenticonRenderer) =
        Avatar.newBuilder().elementRegistry(IdenticonElementRegistry(nineBlockIdenticonRenderer))
}