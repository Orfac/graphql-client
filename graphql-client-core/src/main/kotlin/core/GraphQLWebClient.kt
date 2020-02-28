package core

import reactor.core.publisher.Mono

interface GraphQLWebClient {
    fun send(url : String, body: String): String
    fun sendReactive(url : String, body: String): Mono<String>
}