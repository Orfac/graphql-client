package software.graphql.client

interface HttpSender {
    /**
     * Should return a callback that can be wrapped up with CompletableFuture or something alike.
     */
    fun send(uri: String, body: String): () -> String
}

class GraphQLRequestFailedException(message: String) : RuntimeException("GraphQL request failed: $message")