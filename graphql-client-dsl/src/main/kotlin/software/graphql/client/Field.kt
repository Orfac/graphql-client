package software.graphql.client

@DslMarker
annotation class FieldMarker

@FieldMarker
abstract class Field(private val fieldName: String, vararg arguments: Argument<*>) :
    RenderableEntry("$fieldName${arguments.toList().renderArguments()}") {

    private val requestedFields = mutableListOf<Field>()

    internal fun addField(field: Field) {
        requestedFields += field
    }

    protected fun <T : Field> initField(field: T, init: T.() -> Unit) =
        field.apply(init)
            .also { addField(it) }

    override fun renderIndented(indent: String): String {
        if (requestedFields.isEmpty())
            throw RuntimeException("Invalid query: no subfields of '$fieldName' specified")

        return "$indent$marker {\n" +
                requestedFields.joinToString(separator = "") { it.renderIndented("$indent  ") } +
                "$indent}\n"
    }
}