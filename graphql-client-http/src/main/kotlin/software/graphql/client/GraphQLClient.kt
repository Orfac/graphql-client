package software.graphql.client

import java.util.concurrent.CompletableFuture

class GraphQLClient(val httpSender: HttpSender, val jsonReader: JsonObjectReader) {
    var uri: String = "http://127.0.0.1"

    fun uri(uri: String): GraphQLClient {
        this.uri = uri
        return this
    }

    // Query as argument, because it's GraphqlClient, not just Http
    // D - data, E - errors, X - extensions
    inline fun <reified T : GraphQLResponse> sendQuery(query: Query): CompletableFuture<T> =
        httpSender.send(uri, query.createRequest())
            .thenApplyAsync {
                jsonReader.readObject(it, T::class.java)
            }
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
