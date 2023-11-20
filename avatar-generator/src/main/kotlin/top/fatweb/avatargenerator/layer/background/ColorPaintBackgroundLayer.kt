package top.fatweb.avatargenerator.layer.background

import top.fatweb.avatargenerator.IAvatarInfo
import java.awt.Color
import java.awt.Paint
import java.awt.image.BufferedImage

class ColorPaintBackgroundLayer : AbstractPaintBackgroundLayer {
    private val color: Color

    constructor() : this(Color(0xE2A6FF))

    constructor(color: Color) {
        this.color = color
    }

    override fun buildPaint(avatarInfo: IAvatarInfo, bufferedImage: BufferedImage): Paint {
        return color
    }
}