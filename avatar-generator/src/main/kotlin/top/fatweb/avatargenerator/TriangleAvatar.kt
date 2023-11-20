package top.fatweb.avatargenerator

import java.awt.Color

object TriangleAvatar {
    fun newAvatarBuilder() = Avatar.newBuilder().elementRegistry(TriangleElementRegistry())

    fun newAvatarBuilder(vararg colors: Color) =
        Avatar.newBuilder().elementRegistry(TriangleElementRegistry(8, colors.toList()))
}