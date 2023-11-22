package top.fatweb.avatargenerator.cache

import top.fatweb.avatargenerator.IAvatarInfo
import java.awt.image.BufferedImage
import java.nio.file.Path

/**
 * The public interface for all cache implementation classes.
 *
 * @author FatttSnake, fatttsnake@gmail.com
 * @since 1.0.0
 */
interface ICache {
    /**
     * Public interface for loading cached content
     *
     * @author FatttSnake, fatttsnake@gmail.com
     * @since 1.0.0
     */
    interface ILoader {
        /**
         * Load avatar info
         *
         * @param avatarInfo
         * @return image
         * @author FatttSnake, fatttsnake@gmail.com
         * @since 1.0.0
         * @see IAvatarInfo
         * @see BufferedImage
         */
        fun load(avatarInfo: IAvatarInfo): BufferedImage
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
    fun get(avatarInfo: IAvatarInfo, loader: ILoader): BufferedImage

    companion object {
        /**
         * Use temp dir for cache
         *
         * @return cache instance
         * @author FatttSnake, fatttsnake@gmail.com
         * @since 1.0.0
         * @see ICache
         */
        @JvmStatic
        fun tempCache(): ICache {
            val memoryCache = MemoryCache()
            val fileCache = FileCache()

            return object : ICache {
                override fun get(avatarInfo: IAvatarInfo, loader: ILoader): BufferedImage {
                    return memoryCache.get(avatarInfo, object : ILoader {
                        override fun load(avatarInfo: IAvatarInfo): BufferedImage {
                            return fileCache.get(avatarInfo, loader)
                        }
                    })
                }
            }
        }

        /**
         * Use root dir for cache
         *
         * @param rootPath
         * @return cache instance
         * @author FatttSnake, fatttsnake@gmail.com
         * @since 1.0.0
         * @see Path
         * @see ICache
         */
        @JvmStatic
        fun defaultCache(rootPath: Path): ICache {
            val memoryCache = MemoryCache()
            val fileCache = FileCache(rootPath)

            return object : ICache {
                override fun get(avatarInfo: IAvatarInfo, loader: ILoader): BufferedImage {
                    return memoryCache.get(avatarInfo, object : ILoader {
                        override fun load(avatarInfo: IAvatarInfo): BufferedImage {
                            return fileCache.get(avatarInfo, loader)
                        }
                    })
                }

            }
        }
    }

}