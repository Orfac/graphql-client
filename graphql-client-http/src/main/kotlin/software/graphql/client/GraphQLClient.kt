package software.graphql.client

interface GraphQLClient {
    fun uri(uri: String): GraphQLClient

    /**
     * Sends the query via HttpSender.send(String, String) and tries to construct an object of
     * type GraphQLResponse<D, E> from its return value via JsonObjectReader.readObject<T>(String, TypeResolver<T>).
     *
     * @param D the type of Data expected from GraphQL server to be returned in "data" field
     * @param E the type of Error expected from GraphQL server to be returned in "errors" field
     *
     * @return a function returning GraphQLResponse<D, E> object. This function can be called directly or wrapped
     * up with asynchronous tools like CompletableFuture.
     */
    fun <D : Any, E : GraphQLError> sendQuery(
        query: Query,
        dataType: TypeResolver<GraphQLResponse<D, E>>
    ): Callback<GraphQLResponse<D, E>>
}

/**
 * This is the default implementation of GraphQLClient interface. Its method sendQuery tries to
 * construct an object of type GraphQLResponse<D, E> via JsonObjectReader.readObject<T>(String, TypeResolver<T>)
 * (where D is for Data and E is for Error) from return value of HttpSender.send(String, String) method.
 *
 * In case you need to do some extra manipulations (like saving context of Mono in
 * software.graphql.client.NettyGraphQLClient), consider providing your own implementation of GraphQLClient interface.
 */
class DefaultGraphQLClient(private val httpSender: HttpSender, private val jsonReader: JsonObjectReader) :
    GraphQLClient {

    private var uri: String = "http://127.0.0.1"

    /**
     * Returns a new DefaultGraphQLClient object with same jsonReader and specified uri
     */
    override fun uri(uri: String) = DefaultGraphQLClient(httpSender, jsonReader).apply {
        this.uri = uri
    }

    override fun <D : Any, E : GraphQLError> sendQuery(
        query: Query,
        dataType: TypeResolver<GraphQLResponse<D, E>>
    ): Callback<GraphQLResponse<D, E>> = {
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
