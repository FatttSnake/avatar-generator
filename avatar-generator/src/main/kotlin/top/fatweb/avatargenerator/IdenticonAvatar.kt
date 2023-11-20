package top.fatweb.avatargenerator

import top.fatweb.avatargenerator.element.IdenticonElementRegistry
import top.fatweb.avatargenerator.element.identicon.NineBlockIdenticonRenderer

object IdenticonAvatar {
    fun newAvatarBuilder() = Avatar.newBuilder().elementRegistry(IdenticonElementRegistry())

    fun newAvatarBuilder(nineBlockIdenticonRenderer: NineBlockIdenticonRenderer) =
        Avatar.newBuilder().elementRegistry(IdenticonElementRegistry(nineBlockIdenticonRenderer))
}