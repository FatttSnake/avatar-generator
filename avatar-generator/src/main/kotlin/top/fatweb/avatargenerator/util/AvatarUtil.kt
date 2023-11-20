package top.fatweb.avatargenerator.util

import java.awt.*
import java.awt.image.BufferedImage
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import javax.imageio.ImageIO
import kotlin.math.sqrt

object AvatarUtil {
    val defaultColors = listOf<Color>(
        Color(0x6e1e78),
        Color(0x82be00),
        Color(0xa1006b),
        Color(0x009aa6),
        Color(0xcd0037),
        Color(0x0088ce),
        Color(0xe05206),
        Color(0xd52b1e),
        Color(0xffb612),
        Color(0xd2e100)
    )

    fun activeAntialiasing(graphics2D: Graphics2D) {
        graphics2D.setRenderingHints(
            mapOf(
                RenderingHints.KEY_INTERPOLATION to RenderingHints.VALUE_INTERPOLATION_BICUBIC,
                RenderingHints.KEY_ALPHA_INTERPOLATION to RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY,
                RenderingHints.KEY_RENDERING to RenderingHints.VALUE_RENDER_QUALITY,
                RenderingHints.KEY_ANTIALIASING to RenderingHints.VALUE_ANTIALIAS_ON,
                RenderingHints.KEY_COLOR_RENDERING to RenderingHints.VALUE_COLOR_RENDER_QUALITY
            )
        )
    }

    fun resizeImage(src: BufferedImage, width: Int, height: Int): BufferedImage {
        val originalWidth = src.width
        val originalHeight = src.height

        if (originalWidth == width && originalHeight == height) {
            return src
        }

        val diffComponent = width / height.toDouble()
        val diffImage = originalWidth / originalHeight.toDouble()
        val diff = diffImage / diffComponent

        var w = width
        var h = height
        if (diff >= 1.0) {
            h = (h / diff).toInt()
        } else {
            w = (w * diff).toInt()
        }

        val dest = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        val graphics2D = dest.createGraphics()
        activeAntialiasing(graphics2D)
        graphics2D.drawImage(src.getScaledInstance(w, h, Image.SCALE_SMOOTH), (width - w) / 2, (height - h) / 2, null)
        graphics2D.dispose()

        return dest
    }

    fun fillColorImage(src: BufferedImage, color: Color): BufferedImage {
        val width = src.width
        val height = src.height

        val dest = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        val graphics2D = dest.createGraphics()
        graphics2D.color = color
        graphics2D.fillRect(0, 0, width, height)
        graphics2D.composite = AlphaComposite.DstIn
        graphics2D.drawImage(src, 0, 0, null)
        graphics2D.dispose()

        return dest
    }

    fun tintImage(src: BufferedImage, color: Color): BufferedImage {
        val width = src.width
        val height = src.height

        val dest = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        val graphics2D = dest.createGraphics()
        graphics2D.drawImage(src, 0, 0, null)
        graphics2D.composite = AlphaComposite.SrcAtop
        graphics2D.color = color
        graphics2D.fillRect(0, 0, width, height)
        graphics2D.dispose()

        return dest
    }

    fun planImage(src: BufferedImage, width: Int, height: Int): BufferedImage {
        val originalWidth = src.width
        val originalHeight = src.height

        if (originalWidth == width && originalHeight == height) {
            return src
        }

        val dest = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        val graphics2D = dest.createGraphics()
        graphics2D.drawImage(src, (width - originalWidth) / 2, (height - originalHeight) / 2, null)
        graphics2D.dispose()

        return dest
    }

    fun getComplementColor(color: Color, blackColor: Color, whiteColor: Color): Color {
        val rgba = color.getComponents(null)
        val luminance = 0.2126 * rgba[0] + 0.7152 * rgba[1] + 0.0722 * rgba[2]
        val ratio = (luminance + 0.05) / 0.05

        return if (ratio > 7) blackColor else whiteColor
    }

    fun toARGBImage(src: Image): BufferedImage {
        val dest = BufferedImage(src.getWidth(null), src.getHeight(null), BufferedImage.TYPE_INT_ARGB)
        val graphics2D = dest.createGraphics()
        graphics2D.drawImage(src, 0, 0, null)
        graphics2D.dispose()

        return dest
    }

    fun saveImageInTemp(src: BufferedImage): Path {
        try {
            val path = Files.createTempFile("image", ".png")
            ImageIO.write(src, "png", path.toFile())

            return path
        } catch (e: IOException) {
            throw RuntimeException("Failed to save image", e)
        }
    }

    private fun addZeroes(str: String, digits: Int): String {
        var newStr = str

        for (i in 1..(digits - str.length)) {
            newStr = newStr.prependIndent("0")
        }

        return newStr
    }

    fun extractColor(code: Long): Color {
        val rgb = addZeroes(code.toULong().toString(16), 6).substring(0, 6).toInt(16)

        return Color(rgb)
    }

    fun getColorDistance(color1: Color, color2: Color): Float {
        val dx = (color1.red - color2.red).toDouble()
        val dy = color1.green - color2.green
        val dz = color1.blue - color2.blue

        return sqrt(dx * dx + dy * dy + dz * dz).toFloat()
    }

    fun getComplementaryColor(color: Color) = Color(color.rgb xor 0x00FFFFFF)
}