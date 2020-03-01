package software.graphql.client

interface GraphQLClient {
    fun uri(uri: String): GraphQLClient

    // Query as argument, because it's GraphQLClient, not just Http
    fun <T : Any> sendQuery(query: Query, dataType: TypeResolver<GraphQLResponse<T>>): Callback<GraphQLResponse<T>>
}

class DefaultGraphQLClient(private val httpSender: HttpSender, private val jsonReader: JsonObjectReader) :
    GraphQLClient {

    private var uri: String = "http://127.0.0.1"

    override fun uri(uri: String): DefaultGraphQLClient {
        this.uri = uri
        return this
    }

    override fun <T : Any> sendQuery(
        query: Query,
        dataType: TypeResolver<GraphQLResponse<T>>
    ): Callback<GraphQLResponse<T>> = {
        jsonReader.readObject(
            httpSender.send(uri, query.createRequest())(),
            dataType
        )
    }
}

// Newline characters may not be recognized by server
fun Query.createRequest() = "{\"query\": \"${render().escape()}\"}"
    .flatten()

private fun String.escape() = replace("\"", "\\\"")
