package top.fatweb.avatargenerator

import kotlin.random.Random

interface IAvatarInfo {
    fun getCode(): Long

    fun getRandom(): Random

    fun getWidth(): Int

    fun getHeight(): Int

    fun getMargin(): Int

    fun getPadding(): Int
}