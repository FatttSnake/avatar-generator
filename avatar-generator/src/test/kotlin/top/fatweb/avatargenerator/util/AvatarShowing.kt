package top.fatweb.avatargenerator.util

import top.fatweb.avatargenerator.Avatar
import java.awt.Desktop
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import javax.imageio.ImageIO
import kotlin.math.abs

object AvatarShowing {
    fun showAvatar(avatar: Avatar) {
        showAvatar(avatar, 4, 1)
    }

    fun showAvatar(avatar: Avatar, w: Int, h: Int) {
        val size: Int = avatar.width
        val dest = BufferedImage(size * w, size * h, BufferedImage.TYPE_INT_ARGB)
        val graphics2D: Graphics2D = dest.createGraphics()
        AvatarUtil.activeAntialiasing(graphics2D)
        val random = Random(10)
        for (y in 0 until h) {
            for (x in 0 until w) {
                val code = abs(random.nextLong().toDouble()).toLong()
                graphics2D.drawImage(avatar.create(code), x * size, y * size, size, size, null)
                print("$code, ")
                //g2.setColor(Color.RED);
                //g2.drawRect(x * 128, y * 128, 128, 128);
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
