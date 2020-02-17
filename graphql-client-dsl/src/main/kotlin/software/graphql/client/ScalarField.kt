package software.graphql.client

class ScalarField(fieldName: String, vararg arguments: Argument<*>) : Field(fieldName, *arguments) {
    override fun renderIndented(append: (String) -> Unit, indent: String) = append("$indent${renderHeader()}\n")
}

fun Field.initScalarField(fieldName: String) = ScalarField(fieldName).also { addField(it) }