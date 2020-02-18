package software.graphql.client

class ScalarField(fieldName: String, vararg arguments: Argument<*>) : Field(fieldName, *arguments) {
    override fun renderIndented(indent: String): String = "$indent$marker\n"
}

fun Field.initScalarField(fieldName: String) = ScalarField(fieldName).also { addField(it) }