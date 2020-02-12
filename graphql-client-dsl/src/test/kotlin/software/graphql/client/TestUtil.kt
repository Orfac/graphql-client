package software.graphql.client

/**
 * Removes all characters from input string except for letters, numbers and double quotes; it also replaces tabs,
 * new line characters and multiple spaces with a single space character.
 *
 * Example:
 *
 *      hello,    $
 *          World!  123
 *        "The end?"
 *
 * is converted to `hello World 123 "The end "`
 */
internal fun flattenQuery(query: String): String {
    return query.replace(Regex("[^a-zA-Z0-9\"]"), " ")
        .replace("\t", " ")
        .replace("\n", " ")
        .replace(Regex("  +"), " ")
        .trim()
}