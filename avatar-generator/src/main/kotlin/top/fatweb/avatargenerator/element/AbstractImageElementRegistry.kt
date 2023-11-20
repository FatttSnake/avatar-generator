package top.fatweb.avatargenerator.element

import top.fatweb.avatargenerator.IAvatarInfo
import java.awt.image.BufferedImage

abstract class AbstractImageElementRegistry protected constructor() : ElementRegistry {
    override fun getElementCount(avatarInfo: IAvatarInfo, name: String) = 1

    override fun getElement(avatarInfo: IAvatarInfo, name: String, index: Int) = buildImage(avatarInfo)

    override fun getGroupCount(avatarInfo: IAvatarInfo) = 1

    override fun getGroup(avatarInfo: IAvatarInfo, index: Int) = listOf(ElementInfo.of("github"))

    protected abstract fun buildImage(avatarInfo: IAvatarInfo): BufferedImage
}