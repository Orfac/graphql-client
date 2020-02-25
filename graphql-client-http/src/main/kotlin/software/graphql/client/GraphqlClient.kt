package software.graphql.client

import java.util.concurrent.CompletableFuture


class GraphqlClient(val httpSender: HttpSender, val jsonReader: JsonObjectReader) {
    var uri: String = "http://127.0.0.1"

    fun uri(uri: String): GraphqlClient {
        this.uri = uri
        return this
    }

    inline fun <reified T> sendQuery(query: String): CompletableFuture<T> =
        httpSender.send(uri, query).thenApplyAsync {
            jsonReader.readObject(it, T::class.java)
        }
}