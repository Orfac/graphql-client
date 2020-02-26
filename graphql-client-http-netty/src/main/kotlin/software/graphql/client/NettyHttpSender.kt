package software.graphql.client

import reactor.core.publisher.Mono
import reactor.netty.NettyOutbound
import reactor.netty.http.client.HttpClientRequest
import java.util.concurrent.CompletableFuture

object NettyHttpSender : HttpSender {
    override fun send(uri: String, body: String): CompletableFuture<String> =
        reactor.netty.http.client.HttpClient
            .create()
            .post()
            .uri(uri)
            .send { req: HttpClientRequest?, outbound: NettyOutbound? ->
                req?.header("Content-Type", "application/json")
                outbound?.sendString(Mono.just(body))
            }
            .responseContent()
            .aggregate()
            .asString()
            .`as` {
                CompletableFuture.supplyAsync {
                    it.block() ?: throw RequestFailedException("Could not receive data from: '$uri'")
                }
            }
}