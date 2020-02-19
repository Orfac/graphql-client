package software.graphql.client

fun query(queryName: String = "", init: Query.() -> Unit) = Query(queryName).apply(init)

class Query internal constructor(private val name: String = "") : Field("query $name".trim(), "") {
    internal val declaredFragments = mutableSetOf<Fragment<*>>()

    override fun renderIndented(indent: String): String {
        if (!nameValidOrEmpty(name))
            throw QueryValidationException("Bad query name '$name'")

        return declaredFragments.joinToString(separator = "") { it.renderDeclaration() } +
                super.renderIndented(indent)
    }
}