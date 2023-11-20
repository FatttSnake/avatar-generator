package top.fatweb.avatargenerator.cache

import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import top.fatweb.avatargenerator.IAvatarInfo
import java.awt.image.BufferedImage
import java.util.concurrent.ExecutionException

class MemoryCache : ICache {

    private val cache: Cache<String, BufferedImage> = CacheBuilder.newBuilder().softValues().build()
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