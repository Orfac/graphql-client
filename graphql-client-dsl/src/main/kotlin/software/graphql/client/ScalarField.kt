package software.graphql.client

class ScalarField(
    private val fieldName: String,
    private val fieldAlias: String = "",
    private vararg val arguments: Argument<*>
) : Field(fieldName, fieldAlias, *arguments) {

    override fun renderIndented(indent: String): String {
        val marker = "$fieldName${arguments.toList().renderArguments()}"
            .withAlias(fieldAlias)
        return "$indent$marker\n"
    }

    override fun validate() {
        if (!nameValid(fieldName))
            throw QueryValidationException("Incorrect scalar field name: '$fieldName'")

        if (!nameValidOrEmpty(fieldAlias))
            throw QueryValidationException("Incorrect scalar field alias: '$fieldAlias'")
    }
}

fun Field.initScalarField(fieldName: String, alias: String, vararg arguments: Argument<*>) =
    ScalarField(fieldName, alias, *arguments).also { addChild(it) }