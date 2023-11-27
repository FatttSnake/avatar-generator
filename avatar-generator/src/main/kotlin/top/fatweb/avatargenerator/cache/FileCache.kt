package top.fatweb.avatargenerator.cache

import top.fatweb.avatargenerator.IAvatarInfo
import top.fatweb.avatargenerator.cache.ICache.ILoader
import java.awt.image.BufferedImage
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import javax.imageio.ImageIO

/**
 * File base cache
 *
 * @author FatttSnake, fatttsnake@gmail.com
 * @since 1.0.0
 * @see ICache
 */
class FileCache : ICache {
    private val rootPathDir: Path

    /**
     * Constructor of FileCache
     *
     * @author FatttSnake, fatttsnake@gmail.com
     * @since 1.0.0
     */
    constructor() : super() {
        try {
            this.rootPathDir = Files.createTempDirectory("avatar")
        } catch (e: IOException) {
            throw RuntimeException("Failed to create a temp directory", e)
        }
    }

    /**
     * Constructor of FileCache
     *
     * @param rootPathDir
     * @author FatttSnake, fatttsnake@gmail.com
     * @since 1.0.0
     * @see Path
     */
    constructor(rootPathDir: Path) : super() {
        this.rootPathDir = rootPathDir
    }

    /**
     * Get cache content
     *
     * @param avatarInfo
     * @param loader
     * @return image
     * @author FatttSnake, fatttsnake@gmail.com
     * @since 1.0.0
     * @see IAvatarInfo
     * @see ILoader
     * @see BufferedImage
     */
    override fun get(avatarInfo: IAvatarInfo, loader: ILoader): BufferedImage {
        val dir =
            "${avatarInfo.getWidth()}-${avatarInfo.getHeight()}-${avatarInfo.getMargin()}-${avatarInfo.getPadding()}"
        val path = Paths.get(rootPathDir.toString(), dir)

        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path)
            } catch (e: IOException) {
                throw RuntimeException("Failed to create directories for $path", e)
            }
        }

        val imageName = "${avatarInfo.getSeed()}.png"
        val imagePath = Paths.get(path.toString(), imageName)

        if (Files.exists(imagePath)) {
            try {
                return ImageIO.read(imagePath.toFile())
            } catch (e: IOException) {
                throw RuntimeException("Failed to read image for $imagePath", e)
            }
        } else {
            val bufferedImage = loader.load(avatarInfo)
            try {
                ImageIO.write(bufferedImage, "png", imagePath.toFile())

                return bufferedImage
            } catch (e: IOException) {
                throw RuntimeException("Failed to write image for $imagePath", e)
            }
        }
    }
}