package software.graphql.client

internal fun String.flatten() =
    replace(Regex("[\t\n]"), " ")
        .replace(Regex(" +"), " ")
        .trim()