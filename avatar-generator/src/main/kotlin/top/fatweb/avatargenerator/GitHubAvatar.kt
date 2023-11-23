package top.fatweb.avatargenerator

import top.fatweb.avatargenerator.element.GitHubElementRegistry

/**
 * Util to generate GitHub style avatar
 *
 * @author FatttSnake, fatttsnake@gmail.com
 * @since 1.0.0
 */
object GitHubAvatar {
    /**
     * Create new builder to generate GitHub style avatar
     *
     * @return avatar builder
     * @author FatttSnake, fatttsnake@gmail.com
     * @since 1.0.0
     * @see Avatar.AvatarBuilder
     */
    @JvmStatic
    fun newAvatarBuilder() = Avatar.newBuilder().elementRegistry(GitHubElementRegistry())

    /**
     * Create new builder to generate GitHub style avatar
     *
     * @param size
     * @param precision
     * @return avatar builder
     * @author FatttSnake, fatttsnake@gmail.com
     * @since 1.0.0
     * @see Avatar.AvatarBuilder
     */
    @JvmStatic
    fun newAvatarBuilder(size: Int, precision: Int) =
        Avatar.newBuilder().elementRegistry(GitHubElementRegistry(size, precision))
}