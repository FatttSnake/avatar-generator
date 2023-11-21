package top.fatweb.avatargenerator.util

import top.fatweb.avatargenerator.Avatar
import java.awt.Desktop
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.nio.file.Files
import java.nio.file.Path
import javax.imageio.ImageIO

object AvatarShowing {
    fun showAvatar(vararg avatars: Avatar.AvatarBuilder) {
        showAvatar(8, 2, avatars = avatars)
    }

    fun showAvatar(w: Int, h: Int, size: Int = 128, vararg avatars: Avatar.AvatarBuilder) {
        avatars.forEach { it.size(size, size) }
        val dest = BufferedImage(size * w, size * h, BufferedImage.TYPE_INT_ARGB)
        val graphics2D: Graphics2D = dest.createGraphics()
        AvatarUtil.activeAntialiasing(graphics2D)
        for (y in 0 until h) {
            for (x in 0 until w) {
                val random = (Long.MIN_VALUE..Long.MAX_VALUE).random()
                graphics2D.drawImage(avatars.random().build().create(random), x * size, y * size, size, size, null)
                print("$random, ")
            }
            println()
        }
        graphics2D.dispose()
        showImage(dest)
    }

    fun showImage(bufferedImage: BufferedImage?) {
        try {
            val file: Path = Files.createTempFile("img", ".png")
            ImageIO.write(bufferedImage, "png", file.toFile())
            Desktop.getDesktop().open(file.toFile())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
