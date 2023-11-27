package top.fatweb.avatargenerator

import top.fatweb.avatargenerator.cache.ICache
import top.fatweb.avatargenerator.element.ElementRegistry
import top.fatweb.avatargenerator.layer.ILayer
import top.fatweb.avatargenerator.util.AvatarUtil
import java.awt.Color
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.random.Random

/**
 * Avatar Generator main class
 *
 * @author FatttSnake, fatttsnake@gmail.com
 * @since 1.0.0
 */
class Avatar private constructor() {
    /**
     * Width of final image
     *
     * @author FatttSnake, fatttsnake@gmail.com
     * @since 1.0.0
     */
    var width = 128
        private set
    /**
     * Height of final image
     *
     * @author FatttSnake, fatttsnake@gmail.com
     * @since 1.0.0
     */
    var height = 128
        private set
    /**
     * Margin of final image
     *
     * @author FatttSnake, fatttsnake@gmail.com
     * @since 1.0.0
     */
    var margin = 0
        private set
    /**
     * Padding of final image
     *
     * @author FatttSnake, fatttsnake@gmail.com
     * @since 1.0.0
     */
    var padding = 0
        private set

    private var elementRegistry: ElementRegistry? = null
    private var colorizeFunction: IColorizeFunction? = null
    private var layers: List<ILayer>? = null
    private var cache: ICache? = null

    /**
     * Create avatar image
     *
     * @param seed the seed to generate avatar
     * @return image
     * @author FatttSnake, fatttsnake@gmail.com
     * @since 1.0.0
     * @see BufferedImage
     */
    fun create(seed: Long): BufferedImage {
        val random = Random(seed)
        val avatarInfo: IAvatarInfo = MyAvatarInfo(seed, random)
        return if (cache != null) {
            cache!!.get(avatarInfo, object : ICache.ILoader {
                override fun load(avatarInfo: IAvatarInfo): BufferedImage {
                    return buildAll(avatarInfo)
                }
            })
        } else {
            buildAll(avatarInfo)
        }
    }

    /**
     * Create avatar image as png bytes
     *
     * @param seed the seed to generate avatar
     * @return byte array
     * @author FatttSnake, fatttsnake@gmail.com
     * @since 1.0.0
     * @see ByteArray
     */
    fun createAsPngBytes(seed: Long): ByteArray {
        val src = create(seed)
        try {
            ByteArrayOutputStream().use { byteArrayOutputStream ->
                ImageIO.write(src, "png", byteArrayOutputStream)
                return byteArrayOutputStream.toByteArray()
            }
        } catch (e: IOException) {
            throw AvatarException("Failed to write png for seed=$seed", e)
        }
    }

    /**
     * Create avatar image as png to file
     *
     * @param seed the seed to generate avatar
     * @param file file to write png
     * @author FatttSnake, fatttsnake@gmail.com
     * @since 1.0.0
     * @see File
     */
    fun createAsPngToFile(seed: Long, file: File?) {
        val src = create(seed)
        try {
            ImageIO.write(src, "png", file)
        } catch (e: IOException) {
            throw AvatarException("Failed to write png for seed=$seed", e)
        }
    }

    /**
     * Create avatar as base64 string
     *
     * @author FatttSnake, fatttsnake@gmail.com
     * @since 1.1.0
     */
    @OptIn(ExperimentalEncodingApi::class)
    fun createAsBase64(seed: Long): String {
        return Base64.encode(createAsPngBytes(seed))
    }

    private fun buildAll(avatarInfo: IAvatarInfo): BufferedImage {
        return try {
            var bufferedImage = buildAvatarImage(avatarInfo)
            val wm = width - margin * 2
            val hm = height - margin * 2
            val wmp = wm - padding * 2
            val hmp = hm - padding * 2
            bufferedImage = AvatarUtil.resizeImage(bufferedImage, wmp, hmp)
            bufferedImage = AvatarUtil.planImage(bufferedImage, wm, hm)
            if (layers != null && layers!!.size > 0) {
                for (layer in layers!!) {
                    bufferedImage = layer.apply(avatarInfo, bufferedImage)
                }
            }
            bufferedImage = AvatarUtil.resizeImage(bufferedImage, wm, hm)
            bufferedImage = AvatarUtil.planImage(bufferedImage, width, height)
            bufferedImage
        } catch (e: Exception) {
            throw AvatarException("Failed to build avatar", e)
        }
    }

    @Throws(IOException::class)
    private fun buildAvatarImage(avatarInfo: IAvatarInfo): BufferedImage {
        if (elementRegistry == null) {
            return BufferedImage(
                width - (margin + padding) * 2,
                height - (margin + padding) * 2,
                BufferedImage.TYPE_INT_ARGB
            )
        }
        val random = avatarInfo.getRandom()
        var xmin = Int.MAX_VALUE
        var ymin = Int.MAX_VALUE
        var xmax = Int.MIN_VALUE
        var ymax = Int.MIN_VALUE
        val imageInfos: MutableList<ImageInfo> = ArrayList()
        val groupCount: Int = elementRegistry!!.getGroupCount(avatarInfo)
        if (groupCount > 0) {
            val d = random.nextInt(groupCount)
            val elements = elementRegistry!!.getGroup(avatarInfo, d)
            if (elements.isNotEmpty()) {
                for (element in elements) {
                    val elementCount: Int = elementRegistry!!.getElementCount(avatarInfo, element.name)
                    if (elementCount > 0) {
                        val index = random.nextInt(elementCount)
                        val bufferedImage: BufferedImage =
                            AvatarUtil.toARGBImage(elementRegistry!!.getElement(avatarInfo, element.name, index))
                        xmin = xmin.coerceAtMost(-bufferedImage.width / 2 + element.offsetX)
                        xmax = xmax.coerceAtLeast(bufferedImage.width / 2 + element.offsetX)
                        ymin = ymin.coerceAtMost(-bufferedImage.height / 2 + element.offsetY)
                        ymax = ymax.coerceAtLeast(bufferedImage.height / 2 + element.offsetY)
                        imageInfos.add(ImageInfo(element.name, bufferedImage, element.offsetX, element.offsetY))
                    }
                }
            }
        }
        val w = xmax - xmin
        val h = ymax - ymin
        val dest = BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB)
        val g2 = dest.createGraphics()
        AvatarUtil.activeAntialiasing(g2)
        for (imageInfo in imageInfos) {
            copyImage(g2, avatarInfo, imageInfo, w, h)
        }
        g2.dispose()
        return dest
    }

    @Throws(IOException::class)
    private fun copyImage(g2: Graphics2D, avatarInfo: IAvatarInfo, imageInfo: ImageInfo, width: Int, height: Int) {
        var img = imageInfo.image
        if (colorizeFunction != null) {
            val color = colorizeFunction!!.colorize(avatarInfo, imageInfo.element)
            if (color != null) {
                img = AvatarUtil.tintImage(img, color)
            }
        }
        val w = img.getWidth(null)
        val h = img.getHeight(null)
        val x = (width - w) / 2 + imageInfo.offsetX
        val y = (height - h) / 2 + imageInfo.offsetY
        g2.drawImage(img, x, y, w, h, null)
    }

    /**
     * Public interface for define function to colorize elements
     *
     * @author FatttSnake, fatttsnake@gmail.com
     * @since 1.0.0
     */
    interface IColorizeFunction {
        /**
         * Get color for element
         *
         * @param avatarInfo current avatarInfo
         * @param element    name of element
         * @return color
         * @author FatttSnake, fatttsnake@gmail.com
         * @since 1.0.0
         * @see IAvatarInfo
         * @see Color
         */
        fun colorize(avatarInfo: IAvatarInfo?, element: String?): Color?
    }

    /**
     * Public interface for build avatar
     *
     * @author FatttSnake, fatttsnake@gmail.com
     * @since 1.0.0
     */
    class AvatarBuilder internal constructor() {
        private var width = 128
        private var height = 128
        private var padding = 0
        private var margin = 0
        private var elementRegistry: ElementRegistry? = null
        private var colorizeFunction: IColorizeFunction? = null
        private var layers: List<ILayer>? = null
        private var cache: ICache? = null

        /**
         * Registry element
         *
         * @param elementRegistry
         * @return avatar builder
         * @author FatttSnake, fatttsnake@gmail.com
         * @since 1.0.0
         * @see ElementRegistry
         * @see AvatarBuilder
         *
         */
        fun elementRegistry(elementRegistry: ElementRegistry?): AvatarBuilder {
            this.elementRegistry = elementRegistry
            return this
        }

        /**
         * Set size of avatar
         * Default is 128*128
         *
         * @param size
         * @return avatar builder
         * @author FatttSnake, fatttsnake@gmail.com
         * @since 1.0.0
         * @see AvatarBuilder
         */
        fun size(size: Int) = size(size, size)

        /**
         * Set size of avatar
         * Default is 128*128
         *
         * @param width
         * @param height
         * @return avatar builder
         * @author FatttSnake, fatttsnake@gmail.com
         * @since 1.0.0
         * @see AvatarBuilder
         */
        fun size(width: Int, height: Int): AvatarBuilder {
            this.width = width
            this.height = height
            return this
        }

        /**
         * Set margin of avatar in pixel
         * Default is 8
         *
         * @param margin
         * @return avatar builder
         * @author FatttSnake, fatttsnake@gmail.com
         * @since 1.0.0
         * @see AvatarBuilder
         */
        fun margin(margin: Int): AvatarBuilder {
            this.margin = margin
            return this
        }

        /**
         * Set padding of avatar in pixel
         * Default is 8
         *
         * @param padding
         * @return avatar builder
         * @author FatttSnake, fatttsnake@gmail.com
         * @since 1.0.0
         * @see AvatarBuilder
         */
        fun padding(padding: Int): AvatarBuilder {
            this.padding = padding
            return this
        }

        /**
         * Add layers to apply in elements
         *
         * @param layers
         * @return avatar builder
         * @author FatttSnake, fatttsnake@gmail.com
         * @since 1.0.0
         * @see ILayer
         * @see AvatarBuilder
         */
        fun layers(vararg layers: ILayer): AvatarBuilder {
            this.layers = layers.toList()
            return this
        }

        /**
         * Set the color of all elements
         *
         * @param color
         * @return avatar builder
         * @author FatttSnake, fatttsnake@gmail.com
         * @since 1.0.0
         * @see Color
         * @see AvatarBuilder
         */
        fun color(color: Color?): AvatarBuilder {
            return colorizeFunction(object : IColorizeFunction {
                override fun colorize(avatarInfo: IAvatarInfo?, element: String?): Color? {
                    return color
                }
            })
        }

        /**
         * Set the color of element
         *
         * @param colorizeFunction
         * @return avatar builder
         * @author FatttSnake, fatttsnake@gmail.com
         * @since 1.0.0
         * @see IColorizeFunction
         * @see AvatarBuilder
         */
        fun colorizeFunction(colorizeFunction: IColorizeFunction?): AvatarBuilder {
            this.colorizeFunction = colorizeFunction
            return this
        }

        /**
         * Set cache
         *
         * @param cache
         * @return avatar builder
         * @author FatttSnake, fatttsnake@gmail.com
         * @since 1.0.0
         * @see ICache
         * @see AvatarBuilder
         */
        fun cache(cache: ICache?): AvatarBuilder {
            this.cache = cache
            return this
        }

        /**
         * Build image
         *
         * @return avatar
         * @author FatttSnake, fatttsnake@gmail.com
         * @since 1.0.0
         * @see Avatar
         */
        fun build(): Avatar {
            val avatar = Avatar()
            avatar.width = width
            avatar.height = height
            avatar.margin = margin
            avatar.padding = padding
            avatar.elementRegistry = elementRegistry
            avatar.colorizeFunction = colorizeFunction
            avatar.layers = layers?.toTypedArray()?.copyOf()?.toList()
            avatar.cache = cache

            return avatar
        }
    }

    private class ImageInfo(
        val element: String,
        val image: BufferedImage,
        val offsetX: Int,
        val offsetY: Int
    )

    private inner class MyAvatarInfo(private val seedValue: Long, private val randomValue: Random) : IAvatarInfo {
        override fun getSeed(): Long {
            return seedValue
        }

        override fun getRandom(): Random {
            return randomValue
        }

        override fun getWidth(): Int {
            return width
        }

        override fun getHeight(): Int {
            return height
        }

        override fun getMargin(): Int {
            return margin
        }

        override fun getPadding(): Int {
            return padding
        }
    }

    companion object {
        /**
         * Create new builder
         *
         * @return avatar builder
         * @author FatttSnake, fatttsnake@gmail.com
         * @since 1.0.0
         * @see AvatarBuilder
         */
        @JvmStatic
        fun newBuilder(): AvatarBuilder {
            return AvatarBuilder()
        }
    }
}