package software.graphql.client

internal fun getTestQuery(path: String): String =
    QueryBuilderTest::class.java.getResource("/defaultQueries/$path").readText()