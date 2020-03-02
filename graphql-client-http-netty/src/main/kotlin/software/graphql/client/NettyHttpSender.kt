package software.graphql.client

import io.netty.handler.codec.http.HttpHeaderNames
import reactor.core.publisher.Mono
import reactor.netty.ByteBufFlux
import reactor.netty.http.client.HttpClient

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
                    if (it.getOrDefault(CONTEXT_TEST_ENABLED, false) == true)
                        assert(CONTEXT_TEST_VALUE == it[CONTEXT_TEST_KEY])
                }
            }
}