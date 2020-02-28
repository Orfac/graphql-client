package software.graphql.client

import io.netty.handler.codec.http.HttpHeaders
import reactor.core.publisher.Mono
import reactor.netty.ByteBufFlux
import reactor.netty.http.client.HttpClient

object NettyHttpSender : HttpSender {
    private val client = HttpClient.create()
        .headers { t: HttpHeaders? -> t?.add("Content-Type", "application/json") }

    override fun send(uri: String, body: String): () -> String = {
        client.post()
            .uri(uri)
            .send(ByteBufFlux.fromString(Mono.just(body)))
            .responseContent()
            .aggregate()
            .asString()
            .`as` {
                it.block() ?: throw RequestFailedException("Could not receive data from: '$uri'")
            }
    }
}

fun <T : GraphQLResponse> GraphQLCallback<T>.asMono(): Mono<T> =
    Mono.fromCallable(jsonCallback)
        .map(responseCallback)