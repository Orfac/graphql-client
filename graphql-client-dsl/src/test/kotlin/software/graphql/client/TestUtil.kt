package software.graphql.client

/**
 * Replaces all tabs, newline characters and space sequences with a single space character and [trim]s the result
 */
internal fun String.flatten() =
    replace(Regex("[\t\n]"), " ")
        .replace(Regex(" +"), " ")
        .trim()