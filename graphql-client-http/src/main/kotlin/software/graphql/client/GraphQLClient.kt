package software.graphql.client

import software.graphql.client.netty.NettyHttpSender
import software.graphql.client.netty.addLast
import software.graphql.client.netty.responses

class GraphQLClient(private val httpSender: HttpSender, private val jsonReader: JsonObjectReader) {
    private var uri: String = "http://127.0.0.1"

    fun uri(uri: String): GraphQLClient {
        this.uri = uri
        return this
    }

    // Query as argument, because it's GraphQLClient, not just Http
    fun <T : Any> sendQuery(query: Query, dataType: TypeResolver<GraphQLResponse<T>>): Callback<GraphQLResponse<T>> {
        if (httpSender is NettyHttpSender) {
            val responseMono = httpSender.sendMono(uri, query.createRequest())
                .map { json ->
                    jsonReader.readObject(json, dataType)
                }
            val id = responses.addLast(responseMono)

            return Callback(id, httpSender.callbackFromMono(responseMono, uri))
        }

        return Callback {
            jsonReader.readObject(
                httpSender.send(uri, query.createRequest())(),
                dataType
            )
        }
    }
}

internal const val NO_ID = -1

class Callback<T>(private val callback: () -> T) {
    internal var id: Int = NO_ID

    internal constructor(id: Int, callback: () -> T) : this(callback) {
        this.id = id
    }

    fun call() = callback()
}

// Newline characters may not be recognized by server
private fun Query.createRequest() = "{\"query\": \"${render().escape()}\"}"
    .flatten()

private fun String.escape() = replace("\"", "\\\"")
