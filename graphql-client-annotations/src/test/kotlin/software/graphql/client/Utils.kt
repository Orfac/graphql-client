package software.graphql.client

internal fun getTestQuery(path: String): String =
    GraphQLTest::class.java.getResource("/defaultQueries/$path").readText()