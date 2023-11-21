package top.fatweb.avatargenerator

import top.fatweb.avatargenerator.element.IdenticonElementRegistry
import top.fatweb.avatargenerator.element.identicon.NineBlockIdenticonRenderer

object IdenticonAvatar {
    @JvmStatic
    fun newAvatarBuilder() = Avatar.newBuilder().elementRegistry(IdenticonElementRegistry())

    @JvmStatic
    fun newAvatarBuilder(nineBlockIdenticonRenderer: NineBlockIdenticonRenderer) =
        Avatar.newBuilder().elementRegistry(IdenticonElementRegistry(nineBlockIdenticonRenderer))
}