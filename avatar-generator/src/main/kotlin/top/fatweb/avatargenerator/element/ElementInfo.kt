package top.fatweb.avatargenerator.element

class ElementInfo private constructor(
    val name: String,
    val offsetX: Int,
    val offsetY: Int
) {
    companion object {
        @JvmStatic
        fun of(name: String) = ElementInfo(name, 0, 0)

        @JvmStatic
        fun of(name: String, offsetX: Int, offsetY: Int) = ElementInfo(name, offsetX, offsetY)
    }
}
