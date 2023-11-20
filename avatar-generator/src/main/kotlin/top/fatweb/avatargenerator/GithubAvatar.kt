package top.fatweb.avatargenerator

import top.fatweb.avatargenerator.element.GithubElementRegistry

object GithubAvatar {
    fun newAvatarBuilder() = Avatar.newBuilder().elementRegistry(GithubElementRegistry())

    fun newAvatarBuilder(size: Int, precision: Int) =
        Avatar.newBuilder().elementRegistry(GithubElementRegistry(size, precision))
}