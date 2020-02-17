package software.graphql.client

@DslMarker
annotation class FieldMarker

@FieldMarker
abstract class Field(private val fieldName: String, private vararg val arguments: Argument<*>) : RenderableEntry() {
    private val requestedFields = arrayListOf<Field>()

    internal fun addField(field: Field) {
        requestedFields.add(field)
    }

    protected fun <T : Field> initField(field: T, init: T.() -> Unit) =
        field.apply(init)
            .also { addField(it) }

    internal fun renderHeader() = "$fieldName${arguments.toList().renderArguments()}"

    override fun renderIndented(append: (String) -> Unit, indent: String) {
        if (requestedFields.isEmpty())
            throw RuntimeException("Invalid query: no subfields of '$fieldName' specified")

        append("$indent${renderHeader()} {\n")
        for (field in requestedFields)
            field.renderIndented(append, "$indent  ")
        append("$indent}\n")
    }

}

