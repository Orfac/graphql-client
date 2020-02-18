package software.graphql.client

class ScalarField(private val fieldName: String, private val fieldAlias: String = "", vararg arguments: Argument<*>) :
    Field(fieldName, fieldAlias, *arguments) {
    override fun renderIndented(indent: String): String {
        if (!validateField(fieldName, fieldAlias))
            throw RuntimeException("Invalid query: invalid scalar field identifier: '$fieldAlias: $fieldName'")

        return "$indent$marker\n"
    }
}

fun Field.initScalarField(fieldName: String, alias: String, vararg arguments: Argument<*>) =
    ScalarField(fieldName, alias, *arguments).also { addField(it) }