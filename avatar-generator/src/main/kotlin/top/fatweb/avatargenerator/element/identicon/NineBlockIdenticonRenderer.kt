package top.fatweb.avatargenerator.element.identicon

import top.fatweb.avatargenerator.util.AvatarUtil
import java.awt.Color
import java.awt.Graphics2D
import java.awt.Shape
import java.awt.geom.AffineTransform
import java.awt.geom.Area
import java.awt.geom.GeneralPath
import java.awt.geom.Rectangle2D
import java.awt.image.BufferedImage
import kotlin.math.floor

/**
 * Nine block renderer of indenticon
 *
 * @author FatttSnake, fatttsnake@gmail.com
 * @since 1.0.0
 */
open class NineBlockIdenticonRenderer {
    companion object {
        private const val PATCH_GRIDS = 5
        private const val DEFAULT_PATCH_SIZE = 20.0f
        private const val PATCH_SYMMETRIC: Byte = 1
        private const val PATCH_INVERTED: Byte = 2
        private const val PATCH_MOVETO = -1
    }

    private val patch0 = byteArrayOf(0, 4, 24, 20)

    private val patch1 = byteArrayOf(0, 4, 20)

    private val patch2 = byteArrayOf(2, 24, 20)

    private val patch3 = byteArrayOf(0, 2, 20, 22)

    private val patch4 = byteArrayOf(2, 14, 22, 10)

    private val patch5 = byteArrayOf(0, 14, 24, 22)

    private val patch6 = byteArrayOf(2, 24, 22, 13, 11, 22, 20)

    private val patch7 = byteArrayOf(0, 14, 22)

    private val patch8 = byteArrayOf(6, 8, 18, 16)

    private val patch9 = byteArrayOf(4, 20, 10, 12, 2)

    private val patch10 = byteArrayOf(0, 2, 12, 10)

    private val patch11 = byteArrayOf(10, 14, 22)

    private val patch12 = byteArrayOf(20, 12, 24)

    private val patch13 = byteArrayOf(10, 2, 12)

    private val patch14 = byteArrayOf(0, 2, 10)

    private val patchTypes = arrayOf(
        patch0, patch1, patch2,
        patch3, patch4, patch5, patch6, patch7, patch8, patch9, patch10,
        patch11, patch12, patch13, patch14, patch0
    )

    private val patchFlags = byteArrayOf(
        PATCH_SYMMETRIC, 0, 0, 0,
        PATCH_SYMMETRIC, 0, 0, 0, PATCH_SYMMETRIC, 0, 0, 0, 0, 0, 0,
        (PATCH_SYMMETRIC + PATCH_INVERTED).toByte()
    )

    private val centerPatchTypes = intArrayOf(0, 4, 8, 15)

    private lateinit var patchShapes: Array<GeneralPath?>

    private var patchOffset = 0f

    /**
     * Size of patch
     *
     * @author FatttSnake, fatttsnake@gmail.com
     * @since 1.0.0
     */
    private var patchSize = 0f
        set(value) {
            field = value
            this.patchOffset = value / 2.0f
            val patchScale = value / 4.0f

            patchShapes = arrayOfNulls(patchTypes.size)
            for (i in this.patchTypes.indices) {
                val patch = GeneralPath(GeneralPath.WIND_NON_ZERO)
                var moveTo = true
                val patchVertices = this.patchTypes[i]
                for (j in patchVertices.indices) {
                    val v = patchVertices[j].toInt()
                    if (v == PATCH_MOVETO) {
                        moveTo = true
                    }
                    val vx = v % PATCH_GRIDS * patchScale - patchOffset
                    val vy = floor((v.toFloat() / PATCH_GRIDS).toDouble()).toFloat() * patchScale - patchOffset
                    if (!moveTo) {
                        patch.lineTo(vx, vy)
                    } else {
                        moveTo = false
                        patch.moveTo(vx, vy)
                    }
                }
                patch.closePath()
                patchShapes[i] = patch
            }
        }

    init {
        patchSize = DEFAULT_PATCH_SIZE
    }

    /**
     * Render image
     *
     * @param seed
     * @param size
     * @return image
     * @author FatttSnake, fatttsnake@gmail.com
     * @since 1.0.0
     * @see BufferedImage
     */
    fun render(seed: Int, size: Int) = renderQuilt(seed, size)

    private fun renderQuilt(seed: Int, size: Int): BufferedImage {
        val middleType = centerPatchTypes[seed and 0x3]
        val middleInvert = seed shr 2 and 0x1 != 0
        val cornerType = seed shr 3 and 0x0f
        val cornerInvert = seed shr 7 and 0x1 != 0
        var cornerTurn = seed shr 8 and 0x3
        val sideType = seed shr 10 and 0x0f
        val sideInvert = seed shr 14 and 0x1 != 0
        var sideTurn = seed shr 15 and 0x3
        val blue = seed shr 16 and 0x01f
        val green = seed shr 21 and 0x01f
        val red = seed shr 27 and 0x01f

        val fillColor = Color(red shl 3, green shl 3, blue shl 3)

        val targetImage = BufferedImage(
            size, size,
            BufferedImage.TYPE_INT_ARGB
        )
        val graphics2D = targetImage.createGraphics()
        AvatarUtil.activeAntialiasing(graphics2D)

        val blockSize = size / 3.0f
        val blockSize2 = blockSize * 2.0f

        drawPatch(
            graphics2D, blockSize, blockSize, blockSize, middleType, 0,
            middleInvert, fillColor
        )
        drawPatch(
            graphics2D, blockSize, 0f, blockSize, sideType, sideTurn++, sideInvert,
            fillColor
        )
        drawPatch(
            graphics2D, blockSize2, blockSize, blockSize, sideType, sideTurn++,
            sideInvert, fillColor
        )
        drawPatch(
            graphics2D, blockSize, blockSize2, blockSize, sideType, sideTurn++,
            sideInvert, fillColor
        )
        drawPatch(
            graphics2D, 0f, blockSize, blockSize, sideType, sideTurn++, sideInvert,
            fillColor
        )
        drawPatch(
            graphics2D, 0f, 0f, blockSize, cornerType, cornerTurn++, cornerInvert,
            fillColor
        )
        drawPatch(
            graphics2D, blockSize2, 0f, blockSize, cornerType, cornerTurn++,
            cornerInvert, fillColor
        )
        drawPatch(
            graphics2D, blockSize2, blockSize2, blockSize, cornerType,
            cornerTurn++, cornerInvert, fillColor
        )
        drawPatch(
            graphics2D, 0f, blockSize2, blockSize, cornerType, cornerTurn++,
            cornerInvert, fillColor
        )

        graphics2D.dispose()

        return targetImage
    }

    private fun drawPatch(
        graphics2D: Graphics2D,
        x: Float,
        y: Float,
        size: Float,
        patch: Int,
        turn: Int,
        invert: Boolean,
        fillColor: Color
    ) {
        assert(patch >= 0)
        assert(turn >= 0)

        val patchProcessed = patch % this.patchTypes.size
        val turnProcessed = turn % 4
        val invertProcessed =
            if (this.patchFlags[patchProcessed].toInt() and PATCH_INVERTED.toInt() != 0) {
                !invert
            } else {
                invert
            }

        val shape: Shape = patchShapes[patchProcessed]!!
        val scale = size.toDouble() / patchSize.toDouble()
        val offset = size / 2.0f

        val mask = Area(Rectangle2D.Float(x, y, size, size))
        val hollow = Area(shape).apply {
            transform(AffineTransform().apply {
                translate((x + offset).toDouble(), (y + offset).toDouble())
                scale(scale, scale)
                rotate(Math.toRadians((turnProcessed * 90).toDouble()))
            })
        }
        mask.subtract(hollow)
        graphics2D.color = fillColor
        graphics2D.fill(if (invertProcessed) mask else hollow)
    }
}