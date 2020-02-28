package core

import reactor.core.publisher.Mono

open class GraphQLClient(
    val url: String,
    var webClient: GraphQLWebClient,
    var jsonConverter: GraphQLJsonConverter
) {
    inline fun <reified T> sendQuery(query: String): T {
        val response = webClient.send(url, query)
        return jsonConverter.deserialize(response, T::class.java)
    }

    inline fun <reified T> sendReactiveQuery(query: String): Mono<T> {
        val response = webClient.sendReactive(url, query)
        return jsonConverter.deserializeReactive(response, T::class.java)
    }
}