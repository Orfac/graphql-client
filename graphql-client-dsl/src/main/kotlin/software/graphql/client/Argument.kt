package software.graphql.client

data class Argument<T>(private val name: String, internal val value: T, internal val defaultValue: T? = null) {
    internal fun render(): String {
        if (!validateName(name))
            throw RuntimeException("Invalid query: incorrect argument name: '$name'")
        return "$name: ${renderValue(value)}"
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
        ?.map(Argument<*>::render)
        ?.joinToString(prefix = "(", postfix = ")") { it }
        ?: ""

