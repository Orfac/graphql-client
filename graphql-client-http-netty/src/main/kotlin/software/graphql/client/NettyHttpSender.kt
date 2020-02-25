package software.graphql.client

import reactor.core.publisher.Mono
import reactor.netty.ByteBufFlux
import java.util.concurrent.CompletableFuture

object NettyHttpSender : HttpSender {
    override fun send(uri: String, body: String): CompletableFuture<String> =
        reactor.netty.http.client.HttpClient
            .create()
            .post()
            .uri(uri)
            .send(ByteBufFlux.fromString(Mono.just(body)))
            .responseContent()
            .aggregate()
            .asString()
            .`as` {
                CompletableFuture.supplyAsync {
                    it.block() ?: throw RequestFailedException("Could not receive data from: '$uri'")
                }
            }
}