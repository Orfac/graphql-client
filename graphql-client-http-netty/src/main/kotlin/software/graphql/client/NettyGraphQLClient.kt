package software.graphql.client

import reactor.core.publisher.Mono

class NettyGraphQLClient(private val jsonReader: JsonObjectReader) : GraphQLClient {
    private var uri: String = "http://127.0.0.1"

    /**
     * Returns a new NettyGraphQLClient object with same jsonReader and specified uri
     */
    override fun uri(uri: String) = NettyGraphQLClient(jsonReader).apply {
        this.uri = uri
    }

    override fun <D : Any, E : GraphQLError> sendQuery(
        query: Query,
        dataType: TypeResolver<GraphQLResponse<D, E>>
    ): Callback<GraphQLResponse<D, E>> {
        val responseMono = NettyHttpSender.sendMono(uri, query.createRequest())
            .map { json ->
                jsonReader.readObject(json, dataType)
            }
        val id = responses.addLast(responseMono)

        return NettyCallback(id, callbackFromMono(responseMono, uri))
    }
}

internal fun <T> callbackFromMono(mono: Mono<T>, uri: String): () -> T = {
    mono.`as` {
        it.block() ?: throw GraphQLRequestFailedException("Could not receive data from: '$uri'")
    }
}