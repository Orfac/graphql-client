package software.graphql.client

abstract class RenderableEntry {
    private val builder = StringBuilder()

    internal abstract fun renderIndented(append: (String) -> Unit, indent: String)

    fun render() = builder.apply {
        renderIndented({ append(it) }, "")
    }.toString()
}
