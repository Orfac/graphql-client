package software.graphql.client

import java.util.concurrent.CompletableFuture

interface HttpSender {
    fun send(uri: String, body: String): CompletableFuture<String>
}

class RequestFailedException(message: String) : RuntimeException("Request failed: $message")