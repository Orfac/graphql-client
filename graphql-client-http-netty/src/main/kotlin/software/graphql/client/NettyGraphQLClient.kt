package software.graphql.client

import reactor.core.publisher.Mono

class NettyGraphQLClient(private val jsonReader: JsonObjectReader) : GraphQLClient {
    private var uri: String = "http://127.0.0.1"

    override fun uri(uri: String) = this.apply {
        this.uri = uri
    }

    override fun <T : Any> sendQuery(
        query: Query,
        dataType: TypeResolver<GraphQLResponse<T>>
    ): Callback<GraphQLResponse<T>> {
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
        it.block() ?: throw RequestFailedException("Could not receive data from: '$uri'")
    }
}