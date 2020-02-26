package software.graphql.client

import java.util.concurrent.CompletableFuture

class GraphqlClient(val httpSender: HttpSender, val jsonReader: JsonObjectReader) {
    var uri: String = "http://127.0.0.1"

    fun uri(uri: String): GraphqlClient {
        this.uri = uri
        return this
    }

    // Query as argument, because it's GraphqlClient, not just Http
    inline fun <reified T> sendQuery(query: Query): CompletableFuture<T> =
        httpSender.send(uri, query.createRequest()).thenApplyAsync {
            jsonReader.readObject(it, T::class.java)
        }
}

// Newline characters may not be recognized by server
fun Query.createRequest() = "{\"query\": \"${render()}\"}".flatten()
