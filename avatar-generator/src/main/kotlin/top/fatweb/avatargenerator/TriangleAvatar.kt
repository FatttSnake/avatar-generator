package top.fatweb.avatargenerator

import java.awt.Color

object TriangleAvatar {
    @JvmStatic
    fun newAvatarBuilder() = Avatar.newBuilder().elementRegistry(TriangleElementRegistry())

    @JvmStatic
    fun newAvatarBuilder(vararg colors: Color) =
        Avatar.newBuilder().elementRegistry(TriangleElementRegistry(8, colors.toList()))
}