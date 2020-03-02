package software.graphql.client

/**
 * This function should be used to create a new Query instance.
 */
fun query(queryName: String = "", init: Query.() -> Unit) = Query(queryName).apply(init)

/**
 * This class represents a query. Its fields should be defined as extension functions using methods
 * [software.graphql.client.initField] and [software.graphql.client.initScalarField].
 *
 * See an example in [software.graphql.client.example] package.
 */
class Query internal constructor(private val name: String = "") : Field("query $name".trim(), "") {
    internal val declaredFragments = mutableSetOf<Fragment<*>>()

    override fun renderIndented(indent: String): String {
        if (!nameValidOrEmpty(name))
            throw QueryValidationException("Bad query name '$name'")

        return declaredFragments.joinToString(separator = "") { it.renderDeclaration() } +
                super.renderIndented(indent)
    }
}