package software.graphql.client

@DslMarker
annotation class FieldMarker

@FieldMarker
abstract class Field(
    private val fieldName: String,
    private val fieldAlias: String,
    private vararg val arguments: Argument<*>
) : RenderableEntry() {

    private val requestedChildren = mutableListOf<RenderableEntry>()

    internal fun addChild(child: RenderableEntry) {
        requestedChildren += child
    }

    protected fun <T : Field> initField(field: T, init: T.() -> Unit) =
        field.apply(init)
            .also { addChild(it) }

    override fun renderIndented(indent: String): String {
        val marker = "$fieldName${arguments.toList().renderArguments()}"
            .withAlias(fieldAlias)

        return "$indent$marker {\n" +
                requestedChildren.joinToString(separator = "") { it.render("$indent  ") } +
                "$indent}\n"
    }

    override fun validate() {
        if (requestedChildren.isEmpty())
            throw QueryValidationException("No subfields of '$fieldAlias: $fieldName' specified")

        if (!nameValid(fieldName))
            throw QueryValidationException("Incorrect field name: '$fieldName'")

        if (!nameValidOrEmpty(fieldAlias))
            throw QueryValidationException("Incorrect field alias: '$fieldAlias'")

        requestedChildren.filterIsInstance<Field>().apply {
            forEachIndexed { i, field ->
                // for each field
                subList(i + 1, size).filter {
                    // check every next field
                    field.fieldName == it.fieldName &&
                            field.fieldAlias == it.fieldAlias
                }.forEach {
                    throw QueryValidationException(
                        "Found fields with same name and alias: '${it.fieldAlias}: ${it.fieldName}'"
                    )
                }
            }
        }
    }
}

internal fun String.withAlias(alias: String) =
    if (alias.isNotEmpty())
        "$alias: $this"
    else this
