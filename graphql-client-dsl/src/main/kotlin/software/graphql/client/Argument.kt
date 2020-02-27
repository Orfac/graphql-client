package software.graphql.client

data class Argument<T>(private val name: String, internal val value: T, internal val defaultValue: T? = null) :
    RenderableEntry() {
    override fun renderIndented(indent: String) = "$name: ${renderValue(value)}"

    override fun validate() {
        if (!nameValid(name))
            throw QueryValidationException("Incorrect argument name: '$name'")
    }
}

private fun renderValue(data: Any?): String = when (data) {
    is String -> "\"$data\""
    is Collection<*> -> data.joinToString(prefix = "[", postfix = "]") { renderValue(it) }
    else -> data.toString() // custom types and input objects will go here
}

internal fun Collection<Argument<*>>.renderArguments() =
    filter { it.value != it.defaultValue }
        .takeIf { it.isNotEmpty() }
        ?.map { it.render() }
        ?.joinToString(prefix = "(", postfix = ")") { it }
        ?: ""

