package top.fatweb.avatargenerator

import top.fatweb.avatargenerator.element.GithubElementRegistry

object GitHubAvatar {
    @JvmStatic
    fun newAvatarBuilder() = Avatar.newBuilder().elementRegistry(GithubElementRegistry())

    @JvmStatic
    fun newAvatarBuilder(size: Int, precision: Int) =
        Avatar.newBuilder().elementRegistry(GithubElementRegistry(size, precision))
}