package top.fatweb.avatargenerator.util

import org.junit.jupiter.api.Test
import top.fatweb.avatargenerator.GitHubAvatar
import top.fatweb.avatargenerator.IdenticonAvatar
import top.fatweb.avatargenerator.SquareAvatar
import top.fatweb.avatargenerator.TriangleAvatar
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class)
class AvatarUtilTest {
    @Test
    fun triangleAvatarTest() {
        for (i in 1..5) {
            println(
                Base64.encode(
                    TriangleAvatar.newAvatarBuilder().build()
                        .createAsPngBytes((Long.MIN_VALUE..Long.MAX_VALUE).random())
                )
            )
        }
    }

    @Test
    fun squareAvatarTest() {
        for (i in 1..5) {
            println(
                Base64.encode(
                    SquareAvatar.newAvatarBuilder().build().createAsPngBytes((Long.MIN_VALUE..Long.MAX_VALUE).random())
                )
            )
        }
    }

    @Test
    fun identiconAvatarTest() {
        for (i in 1..5) {
            println(
                Base64.encode(
                    IdenticonAvatar.newAvatarBuilder().build()
                        .createAsPngBytes((Long.MIN_VALUE..Long.MAX_VALUE).random())
                )
            )
        }
    }

    @Test
    fun githubAvatarTest() {
        for (i in 1..5) {
            println(
                Base64.encode(
                    GitHubAvatar.newAvatarBuilder(size = 396, precision = 5).build()
                        .createAsPngBytes((Long.MIN_VALUE..Long.MAX_VALUE).random())
                )
            )
        }
    }
}