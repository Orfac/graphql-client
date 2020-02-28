package software.graphql.client

import java.util.concurrent.CompletableFuture

class GraphQLClient(val httpSender: HttpSender, val jsonReader: JsonObjectReader) {
    var uri: String = "http://127.0.0.1"

    fun uri(uri: String): GraphQLClient {
        this.uri = uri
        return this
    }

    // Query as argument, because it's GraphQLClient, not just Http
    inline fun <reified T : GraphQLResponse> sendQuery(query: Query): GraphQLCallback<T> =
        GraphQLCallback(httpSender.send(uri, query.createRequest()),
            { jsonReader.readObject(it, T::class.java) })
}

class GraphQLCallback<T : GraphQLResponse>(val jsonCallback: () -> String, val responseCallback: (String) -> T?) {
    fun asCompletableFuture(): CompletableFuture<T?> =
        CompletableFuture.supplyAsync(jsonCallback)
            .thenApplyAsync(responseCallback)
}

interface GraphQLResponse {
    val data: Any?
    val errors: Collection<GraphQLError>?
    val extensions: Any?
}

interface GraphQLError {
    val message: String
}

data class PlainTextGraphQLError(override val message: String) : GraphQLError

// Newline characters may not be recognized by server
fun Query.createRequest() = "{\"query\": \"${render().escape()}\"}"
    .flatten()

private fun String.escape() = replace("\"", "\\\"")
