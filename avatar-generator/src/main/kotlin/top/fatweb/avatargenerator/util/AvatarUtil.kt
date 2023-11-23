package top.fatweb.avatargenerator.util

import java.awt.*
import java.awt.image.BufferedImage
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import javax.imageio.ImageIO
import kotlin.math.sqrt

/**
 * Util for avatar
 *
 * @author FatttSnake, fatttsnake@gmail.com
 * @since 1.0.0
 */
object AvatarUtil {
    /**
     * Default colors
     *
     * @author FatttSnake, fatttsnake@gmail.com
     * @since 1.0.0
     */
    val defaultColors = listOf(
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

    /**
     * Active antialiasing
     *
     * @param graphics2D
     * @author FatttSnake, fatttsnake@gmail.com
     * @since 1.0.0
     * @see Graphics2D
     */
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

    /**
     * Resize image
     *
     * @param src
     * @param width
     * @param height
     * @return image
     * @author FatttSnake, fatttsnake@gmail.com
     * @since 1.0.0
     * @see BufferedImage
     */
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

    /**
     * Fill image with color
     *
     * @param src
     * @param color
     * @return image
     * @author FatttSnake, fatttsnake@gmail.com
     * @since 1.0.0
     * @see Color
     * @see BufferedImage
     */
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

    /**
     * Tint image with color
     *
     * @param src
     * @param color
     * @return image
     * @author FatttSnake, fatttsnake@gmail.com
     * @since 1.0.0
     * @see BufferedImage
     * @see Color
     */
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

    /**
     * Plan image
     *
     * @param src
     * @param width
     * @param height
     * @return image
     * @author FatttSnake, fatttsnake@gmail.com
     * @since 1.0.0
     * @see BufferedImage
     */
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

    /**
     * Get complement color
     *
     * @param color
     * @param blackColor
     * @param whiteColor
     * @return color
     * @author FatttSnake, fatttsnake@gmail.com
     * @since 1.0.0
     * @see Color
     */
    fun getComplementColor(color: Color, blackColor: Color, whiteColor: Color): Color {
        val rgba = color.getComponents(null)
        val luminance = 0.2126 * rgba[0] + 0.7152 * rgba[1] + 0.0722 * rgba[2]
        val ratio = (luminance + 0.05) / 0.05

        return if (ratio > 7) blackColor else whiteColor
    }

    /**
     * Convert to ARGB image
     *
     * @param src
     * @return image
     * @author FatttSnake, fatttsnake@gmail.com
     * @since 1.0.0
     * @see Image
     * @see BufferedImage
     */
    fun toARGBImage(src: Image): BufferedImage {
        val dest = BufferedImage(src.getWidth(null), src.getHeight(null), BufferedImage.TYPE_INT_ARGB)
        val graphics2D = dest.createGraphics()
        graphics2D.drawImage(src, 0, 0, null)
        graphics2D.dispose()

        return dest
    }

    /**
     * Save image in temp path
     *
     * @param src
     * @return path
     * @author FatttSnake, fatttsnake@gmail.com
     * @since 1.0.0
     * @see BufferedImage
     * @see Path
     */
    fun saveImageInTemp(src: BufferedImage): Path {
        try {
            val path = Files.createTempFile("image", ".png")
            ImageIO.write(src, "png", path.toFile())

            return path
        } catch (e: IOException) {
            throw RuntimeException("Failed to save image", e)
        }
    }

    /**
     * Pad 0 in front of number
     *
     * @author FatttSnake, fatttsnake@gmail.com
     * @since 1.0.0
     */
    private fun addZeroes(str: String, digits: Int): String {
        var newStr = str

        for (i in 1..(digits - str.length)) {
            newStr = newStr.prependIndent("0")
        }

        return newStr
    }

    /**
     * Extract color by seed
     *
     * @param seed
     * @return color
     * @author FatttSnake, fatttsnake@gmail.com
     * @since 1.0.0
     * @see Color
     */
    fun extractColor(seed: Long): Color {
        val rgb = addZeroes(seed.toULong().toString(16), 6).substring(0, 6).toInt(16)

        return Color(rgb)
    }

    /**
     * Get distance between two color
     *
     * @param color1
     * @param color2
     * @return float
     * @author FatttSnake, fatttsnake@gmail.com
     * @since 1.0.0
     * @see Color
     */
    fun getColorDistance(color1: Color, color2: Color): Float {
        val dx = (color1.red - color2.red).toDouble()
        val dy = color1.green - color2.green
        val dz = color1.blue - color2.blue

        return sqrt(dx * dx + dy * dy + dz * dz).toFloat()
    }

    /**
     * Get complementary color
     *
     * @param color
     * @return color
     * @author FatttSnake, fatttsnake@gmail.com
     * @since 1.0.0
     * @see Color
     */
    fun getComplementaryColor(color: Color) = Color(color.rgb xor 0x00FFFFFF)
}