package top.fatweb.avatargenerator.element

import org.reflections.Reflections
import org.reflections.scanners.Scanners
import top.fatweb.avatargenerator.AvatarException
import top.fatweb.avatargenerator.IAvatarInfo
import java.awt.Image
import java.io.IOException
import java.net.URL
import java.util.regex.Pattern
import java.util.stream.Collectors
import javax.imageio.ImageIO

/**
 * URL element registry
 *
 * @author FatttSnake, fatttsnake@gmail.com
 * @since 1.0.0
 * @see AbstractElementRegistry
 */
class URLElementRegistry : AbstractElementRegistry() {
    companion object {
        private val PNG_PATTERN = Pattern.compile(".*.png")

        /**
         * Load all urls with png
         *
         * @param path where is png files
         * @return list of url
         * @author FatttSnake, fatttsnake@gmail.com
         * @since 1.0.0
         * @see ClassLoader
         * @see URL
         */
        @JvmStatic
        fun lsPngURLs(classLoader: ClassLoader, path: String?): List<URL> {
            val reflections = Reflections(path, Scanners.Resources, classLoader)
            val ss: Set<String> = reflections.getResources(PNG_PATTERN)
            return ss.stream().sorted { obj: String, anotherString: String? ->
                obj.compareTo(
                    anotherString!!
                )
            }.map { name: String? ->
                classLoader.getResource(
                    name
                )
            }.collect(Collectors.toList())
        }
    }

    private val elementMap: MutableMap<String, List<URL>?> = mutableMapOf()

    override fun getElementCount(avatarInfo: IAvatarInfo, name: String) =
        if (elementMap.containsKey(name)) elementMap[name]!!.size else 0

    override fun getElement(avatarInfo: IAvatarInfo, name: String, index: Int): Image {
        val url = elementMap[name]!![index]
        return try {
            ImageIO.read(url)
        } catch (e: IOException) {
            throw AvatarException("Failed to load image $url", e)
        }
    }

    /**
     * Put element
     *
     * @param name
     * @param urls
     * @author FatttSnake, fatttsnake@gmail.com
     * @since 1.0.0
     * @see URL
     */
    fun putElement(name: String, urls: List<URL>?) {
        elementMap[name] = if (urls != null) ArrayList<URL>(urls) else null
    }
}