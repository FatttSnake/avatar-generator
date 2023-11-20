package top.fatweb.avatargenerator.element

import top.fatweb.avatargenerator.IAvatarInfo
import java.awt.Image

interface ElementRegistry {
    fun getElementCount(avatarInfo: IAvatarInfo, name: String): Int

    fun getElement(avatarInfo: IAvatarInfo, name: String, index: Int): Image

    fun getGroupCount(avatarInfo: IAvatarInfo): Int

    fun getGroup(avatarInfo: IAvatarInfo, index: Int): List<ElementInfo>
}