package software.graphql.client

abstract class RenderableEntry {
    internal abstract fun renderMarker(): String
    private fun renderNested(builder: StringBuilder, indent: String) {
        for (nested in nestedEntries())
            nested.render(builder, "$indent  ")
    }

    internal abstract fun nestedEntries(): List<RenderableEntry>

    internal open fun render(builder: StringBuilder, indent: String) {
        builder.append("$indent${renderMarker()} {\n")
        renderNested(builder, "$indent  ")
        builder.append("$indent}\n")
    }

    fun render() = StringBuilder().apply { render(this, "") }.toString()

    override fun toString() = StringBuilder().apply {
        append("${renderMarker()} {\n")
        for (nested in nestedEntries()) {
            append("  ${nested.renderMarker()}")
            if (nested.nestedEntries().isNotEmpty())
                append(" {...}")
            append("\n")
        }
        append("}\n")
    }.toString()
}

@DslMarker
annotation class FieldMarker

@FieldMarker
abstract class Field(private val fieldName: String, private vararg val arguments: Argument<*>) : RenderableEntry() {
    private val requestedFields = arrayListOf<Field>()

    protected fun <T : Field> initField(field: T, init: T.() -> Unit) =
        field.apply(init)
            .also { requestedFields.add(it) }

    protected fun initScalarField(fieldName: String) = ScalarField(fieldName).also { requestedFields.add(it) }

    override fun renderMarker() = "$fieldName${renderArguments()}"

    private fun renderArguments() =
        arguments.filter { it.isNotDefault() }
            .takeIf { it.isNotEmpty() }
            ?.map(Argument<*>::toString)
            ?.joinToString(prefix = "(", postfix = ")") { it }
            ?: ""

    override fun nestedEntries(): List<RenderableEntry> = requestedFields

    override fun render(builder: StringBuilder, indent: String) {
        if (this !is ScalarField && nestedEntries().isEmpty())
            throw RuntimeException("Invalid query: no subfields of '$fieldName' specified")
        super.render(builder, indent)
    }
}

class ScalarField(fieldName: String, vararg arguments: Argument<*>) : Field(fieldName, *arguments) {
    override fun nestedEntries() = emptyList<RenderableEntry>()

    override fun render(builder: StringBuilder, indent: String) {
        builder.append("$indent${renderMarker()}\n")
    }

    override fun toString() = renderMarker()
}

class Argument<T>(private val name: String, private val value: T?, private val defaultValue: T? = null) {
    override fun toString() = "$name: ${renderValue(value)}"

    fun isNotDefault() = value != defaultValue

    companion object {
        private fun renderValue(data: Any?): String = when (data) {
            is String -> "\"$data\""
            is Collection<*> -> data.joinToString(prefix = "[", postfix = "]") { renderValue(it) }
            else -> data.toString() // custom types and input objects will go here
        }
    }
}