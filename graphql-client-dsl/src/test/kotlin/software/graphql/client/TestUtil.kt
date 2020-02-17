package software.graphql.client

import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Replaces all tabs, newline characters and space sequences with a single space character and [trim]s the result
 */
internal fun String.flatten() =
    replace(Regex("[\t\n]"), " ")
        .replace(Regex(" +"), " ")
        .trim()

internal fun getTestResource(fileName: String) = PersonBookArgumentsTest::class.java
    .getResourceAsStream(fileName)
    .let { BufferedReader(InputStreamReader(it)) }
    .readText()