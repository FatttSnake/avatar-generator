package top.fatweb.avatargenerator.util

import org.junit.jupiter.api.Test
import top.fatweb.avatargenerator.GitHubAvatar
import top.fatweb.avatargenerator.IdenticonAvatar
import top.fatweb.avatargenerator.SquareAvatar
import top.fatweb.avatargenerator.TriangleAvatar

class AvatarUtilTest {
    @Test
    fun triangleAvatarTest() {
        for (i in 1..5) {
            println(
                TriangleAvatar.newAvatarBuilder().build().createAsBase64((Long.MIN_VALUE..Long.MAX_VALUE).random())
            )
        }
    }

    @Test
    fun squareAvatarTest() {
        for (i in 1..5) {
            println(
                SquareAvatar.newAvatarBuilder().build().createAsBase64((Long.MIN_VALUE..Long.MAX_VALUE).random())
            )
        }
    }

    @Test
    fun identiconAvatarTest() {
        for (i in 1..5) {
            println(
                IdenticonAvatar.newAvatarBuilder().build().createAsBase64((Long.MIN_VALUE..Long.MAX_VALUE).random())
            )
        }
    }

    @Test
    fun githubAvatarTest() {
        for (i in 1..5) {
            println(
                GitHubAvatar.newAvatarBuilder(size = 396, precision = 5).build()
                    .createAsBase64((Long.MIN_VALUE..Long.MAX_VALUE).random())
            )
        }
    }
}