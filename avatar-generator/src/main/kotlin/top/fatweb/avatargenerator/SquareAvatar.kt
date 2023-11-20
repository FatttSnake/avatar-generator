package top.fatweb.avatargenerator

import top.fatweb.avatargenerator.element.SquareElementRegistry
import java.awt.Color

object SquareAvatar {
    fun newAvatarBuilder() = Avatar.newBuilder().elementRegistry(SquareElementRegistry())

    fun newAvatarBuilder(vararg colors: Color) =
        Avatar.newBuilder().elementRegistry(SquareElementRegistry(3, colors.toList()))
}