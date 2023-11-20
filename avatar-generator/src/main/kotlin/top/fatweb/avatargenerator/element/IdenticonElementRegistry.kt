package top.fatweb.avatargenerator.element

import top.fatweb.avatargenerator.IAvatarInfo
import top.fatweb.avatargenerator.element.identicon.NineBlockIdenticonRenderer
import java.awt.image.BufferedImage
import java.math.BigDecimal

class IdenticonElementRegistry(
    private val nineBlockIdenticonRenderer: NineBlockIdenticonRenderer
) : AbstractImageElementRegistry() {
    constructor() : this(NineBlockIdenticonRenderer())

    override fun buildImage(avatarInfo: IAvatarInfo): BufferedImage {

        val code = BigDecimal(avatarInfo.getCode()).remainder(BigDecimal(2).pow(32)).toInt()
        val size =
            (avatarInfo.getWidth() - (avatarInfo.getMargin() + avatarInfo.getPadding()) * 2).coerceAtMost(avatarInfo.getHeight() - (avatarInfo.getMargin() + avatarInfo.getPadding()) * 2)

        return nineBlockIdenticonRenderer.render(code, size)
    }
}