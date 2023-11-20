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
import kotlin.random.Random

class Avatar private constructor() {
    var width = 128
        private set
    var height = 128
        private set
    var padding = 0
        private set
    var margin = 0
        private set
    private var elementRegistry: ElementRegistry? = null
    private var colorizeFunction: IColorizeFunction? = null
    private var layers: List<ILayer>? = null
    private var cache: ICache? = null

    /**
     * Create avatar image
     *
     * @param code the code
     * @return image
     */
    fun create(code: Long): BufferedImage {
        val random = Random(code)
        val avatarInfo: IAvatarInfo = MyAvatarInfo(code, random)
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
     * @param code the code
     * @return byte array
     */
    fun createAsPngBytes(code: Long): ByteArray {
        val src = create(code)
        try {
            ByteArrayOutputStream().use { baos ->
                ImageIO.write(src, "png", baos)
                return baos.toByteArray()
            }
        } catch (e: IOException) {
            throw AvatarException("Failed to write png for code=$code", e)
        }
    }

    /**
     * Create avatar image as png to file
     *
     * @param code the code
     * @param file file to write png
     */
    fun createAsPngToFile(code: Long, file: File?) {
        val src = create(code)
        try {
            ImageIO.write(src, "png", file)
        } catch (e: IOException) {
            throw AvatarException("Failed to write png for code=$code", e)
        }
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

    interface IColorizeFunction {
        /**
         * Get color for element
         *
         * @param avatarInfo current avatarInfo
         * @param element    name of element
         * @return color
         */
        fun colorize(avatarInfo: IAvatarInfo?, element: String?): Color?
    }

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
         * Element loader
         */
        fun elementRegistry(elementRegistry: ElementRegistry?): AvatarBuilder {
            this.elementRegistry = elementRegistry
            return this
        }

        /**
         * Set size of avatar
         * Default is 128x128
         */
        fun size(width: Int, height: Int): AvatarBuilder {
            this.width = width
            this.height = height
            return this
        }

        /**
         * Set space with border
         * Default is 8
         */
        fun padding(padding: Int): AvatarBuilder {
            this.padding = padding
            return this
        }

        /**
         * Set space out border
         * Default is 8
         */
        fun margin(margin: Int): AvatarBuilder {
            this.margin = margin
            return this
        }

        /**
         * Apply layers after
         */
        fun layers(vararg layers: ILayer): AvatarBuilder {
            this.layers = layers.toList()
            return this
        }

        /**
         * Color of element
         */
        fun color(color: Color?): AvatarBuilder {
            return colorizeFunction(object : IColorizeFunction {
                override fun colorize(avatarInfo: IAvatarInfo?, element: String?): Color? {
                    return color
                }
            })
        }

        /**
         * Color of element
         */
        fun colorizeFunction(colorizeFunction: IColorizeFunction?): AvatarBuilder {
            this.colorizeFunction = colorizeFunction
            return this
        }

        fun cache(cache: ICache?): AvatarBuilder {
            this.cache = cache
            return this
        }

        /**
         * Build image
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

    private inner class MyAvatarInfo(private val codeValue: Long, private val randomValue: Random) : IAvatarInfo {
        override fun getCode(): Long {
            return codeValue
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
        fun newBuilder(): AvatarBuilder {
            return AvatarBuilder()
        }
    }
}