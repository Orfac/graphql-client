package software.graphql.client

fun flattenQuery(query: String): String {
    return query.replace(Regex("[^a-zA-Z0-9]"), " ")
        .replace("\t", " ")
        .replace(Regex("  +"), " ")
        .trim()
}