package top.fatweb.avatargenerator.element

import top.fatweb.avatargenerator.IAvatarInfo

abstract class AbstractElementRegistry : ElementRegistry {
    private val groups = mutableListOf<List<ElementInfo>>()

    fun putGroup(vararg elementInfo: ElementInfo) {
        groups.add(elementInfo.toList())
    }

    override fun getGroupCount(avatarInfo: IAvatarInfo) = groups.size

    override fun getGroup(avatarInfo: IAvatarInfo, index: Int) = groups[index]
}