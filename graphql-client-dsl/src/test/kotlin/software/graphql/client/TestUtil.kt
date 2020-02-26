package software.graphql.client

import software.graphql.client.features.ArgumentsTest
import java.io.BufferedReader
import java.io.InputStreamReader

internal fun getTestResource(fileName: String) = ArgumentsTest::class.java
    .getResourceAsStream("/test$fileName")
    .let { BufferedReader(InputStreamReader(it)) }
    .readText()