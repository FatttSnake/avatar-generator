package top.fatweb.avatargenerator.element

class ElementInfo private constructor(
    val name: String,
    val offsetX: Int,
    val offsetY: Int
) {
    companion object {
        fun of(name: String) = ElementInfo(name, 0, 0)

        fun of(name: String, offsetX: Int, offsetY: Int) = ElementInfo(name, offsetX, offsetY)
    }
}
