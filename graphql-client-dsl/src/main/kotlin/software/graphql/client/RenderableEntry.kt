package software.graphql.client

abstract class RenderableEntry {
    protected abstract fun renderIndented(indent: String): String
    protected abstract fun validate()

    fun render(indent: String = ""): String {
        validate()
        return renderIndented(indent)
    }
}

private val nameRegex = Regex("[_A-Za-z][_0-9A-Za-z]*")

fun nameValid(name: String) = name.matches(nameRegex)
fun nameValidOrEmpty(name: String) = name.isEmpty() || nameValid(name)

class QueryValidationException(message: String) : RuntimeException("Query validation exception: $message")

/**
 * Replaces all tabs, newline characters and space sequences with a single space character and [trim]s the result
 */
fun String.flatten() =
    replace(whitespace, " ")
        .replace(spaceSequence, " ")
        .trim()

private val whitespace = Regex("[\t\n\r]")
private val spaceSequence = Regex(" +")