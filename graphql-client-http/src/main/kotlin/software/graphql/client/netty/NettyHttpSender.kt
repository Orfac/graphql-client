package software.graphql.client.netty

import io.netty.handler.codec.http.HttpHeaderNames
import reactor.core.publisher.Mono
import reactor.netty.ByteBufFlux
import reactor.netty.http.client.HttpClient
import software.graphql.client.Callback
import software.graphql.client.HttpSender
import software.graphql.client.NO_ID
import software.graphql.client.RequestFailedException

internal const val CONTEXT_TEST_ENABLED = "CONTEXT_TEST"
internal const val CONTEXT_TEST_KEY = "CONTEXT_TEST_KEY"
internal const val CONTEXT_TEST_VALUE = "CONTEXT_TEST_VALUE"

object NettyHttpSender : HttpSender {
    private val client = HttpClient.create()
        .headers { it[HttpHeaderNames.CONTENT_TYPE] = "application/json" }

    override fun send(uri: String, body: String): () -> String =
        callbackFromMono(sendMono(uri, body), uri)

    internal fun sendMono(uri: String, body: String): Mono<String> =
        client.post()
            .uri(uri)
            .send(ByteBufFlux.fromString(Mono.just(body)))
            .responseContent()
            .aggregate()
            .asString()
            .subscriberContext {
                // adding context test
                it.also {
                    if (!it.isEmpty && it.get<Boolean>(CONTEXT_TEST_ENABLED) == true)
                        assert(CONTEXT_TEST_VALUE == it[CONTEXT_TEST_KEY])
                }
            }

    internal fun <T> callbackFromMono(mono: Mono<T>, uri: String): () -> T = {
        mono.`as` {
            it.block() ?: throw RequestFailedException("Could not receive data from: '$uri'")
        }
    }
}

internal val responses = mutableListOf<Mono<out Any?>>()
internal fun <T> MutableCollection<T>.addLast(element: T) = synchronized(this) {
    size.also {
        add(element)
    }
}

fun <T : Any?> Callback<T>.asMono(): Mono<T> =
    Mono.fromCallable(this::call)
        .let {
            if (id != NO_ID)
                @Suppress("UNCHECKED_CAST")
                responses[id] as? Mono<T> ?: it
            else it
        }
