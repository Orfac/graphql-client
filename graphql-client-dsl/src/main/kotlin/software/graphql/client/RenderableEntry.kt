package software.graphql.client

abstract class RenderableEntry(protected val marker: String) {
    internal abstract fun renderIndented(indent: String = ""): String

    fun render() = renderIndented()
}
