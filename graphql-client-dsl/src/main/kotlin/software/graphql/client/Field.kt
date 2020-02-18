package software.graphql.client

@DslMarker
annotation class FieldMarker

@FieldMarker
abstract class Field(
    private val fieldName: String,
    private val fieldAlias: String,
    vararg arguments: Argument<*>
) : RenderableEntry(
    "$fieldName${arguments.toList().renderArguments()}"
        .withAlias(fieldAlias)
) {

    private val requestedFields = mutableListOf<Field>()

    internal fun addField(field: Field) {
        if (requestedFields.any { it.fieldName == field.fieldName && it.fieldAlias == field.fieldAlias })
            throw RuntimeException(
                "Invalid query: found fields with same name and alias: " +
                        "'${field.fieldAlias}: ${field.fieldName}'"
            )

        requestedFields += field
    }

    protected fun <T : Field> initField(field: T, init: T.() -> Unit) =
        field.apply(init)
            .also { addField(it) }

    override fun renderIndented(indent: String): String {
        if (requestedFields.isEmpty())
            throw RuntimeException("Invalid query: no subfields of '$fieldAlias: $fieldName' specified")

        if (!validateField(fieldName, fieldAlias))
            throw RuntimeException("Invalid query: incorrect field identifier: '$fieldAlias: $fieldName'")

        return "$indent$marker {\n" +
                requestedFields.joinToString(separator = "") { it.renderIndented("$indent  ") } +
                "$indent}\n"
    }
}

internal fun String.withAlias(alias: String) =
    if (alias.isNotEmpty())
        "$alias: $this"
    else this
