package top.fatweb.avatargenerator.cache

import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import top.fatweb.avatargenerator.IAvatarInfo
import top.fatweb.avatargenerator.cache.ICache.ILoader
import java.awt.image.BufferedImage
import java.util.concurrent.ExecutionException

/**
 * Memory base cache
 *
 * @author FatttSnake, fatttsnake@gmail.com
 * @see ICache
 * @since 1.0.0
 */
class MemoryCache : ICache {

    private val cache: Cache<String, BufferedImage> = CacheBuilder.newBuilder().softValues().build()

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
    override fun get(avatarInfo: IAvatarInfo, loader: ICache.ILoader): BufferedImage {
        val key =
            "${avatarInfo.getWidth()}-${avatarInfo.getHeight()}-${avatarInfo.getMargin()}-${avatarInfo.getPadding()}"

        try {
            return cache.get(key) { loader.load(avatarInfo) }
        } catch (e: ExecutionException) {
            throw RuntimeException("Failed to get image from $key", e)
        }
    }
}