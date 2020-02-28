import core.GraphQLWebClient
import reactor.core.publisher.Mono
import reactor.netty.NettyOutbound
import reactor.netty.http.client.HttpClient
import reactor.netty.http.client.HttpClientRequest

class DefaultGraphQLWebClient(val client: HttpClient) : GraphQLWebClient {
    override fun send(url: String, body: String): String {
        return sendReactive(url, body).block() ?: ""
    }

    override fun sendReactive(url: String, body: String): Mono<String> {
        return client.post()
            .uri(url)
            .send { _: HttpClientRequest?, outbound: NettyOutbound? ->
                outbound?.sendString(Mono.just(body))
            }
            .responseContent()
            .aggregate().asString()
    }

}