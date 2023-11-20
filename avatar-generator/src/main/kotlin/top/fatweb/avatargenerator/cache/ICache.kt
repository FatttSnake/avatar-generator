package top.fatweb.avatargenerator.cache

import top.fatweb.avatargenerator.IAvatarInfo
import java.awt.image.BufferedImage
import java.nio.file.Path

interface ICache {
    interface ILoader {
        fun load(avatarInfo: IAvatarInfo): BufferedImage
    }

    fun get(avatarInfo: IAvatarInfo, loader: ILoader): BufferedImage

    companion object {
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