package software.graphql.client

class GraphQLClient(val httpSender: HttpSender, val jsonReader: JsonObjectReader) {
    var uri: String = "http://127.0.0.1"

    fun uri(uri: String): GraphQLClient {
        this.uri = uri
        return this
    }

    // Query as argument, because it's GraphQLClient, not just Http
    inline fun <reified T> sendQuery(query: Query): GraphQLCallback<GraphQLResponse<T>> {
        return GraphQLCallback(httpSender.send(uri, query.createRequest()),
            { jsonReader.readObject(it, GraphQLResponse<T>()::class.java) })
    }
}

class GraphQLCallback<T : GraphQLResponse<*>>(val jsonCallback: () -> String, val responseCallback: (String) -> T) {
    fun call(): T = responseCallback(jsonCallback())
}

open class GraphQLResponse<T>(
    val data: T? = null,
    val errors: Collection<GraphQLError>? = null,
    val extensions: Any? = null
)

interface GraphQLError {
    val message: String
}

data class PlainTextGraphQLError(override val message: String) : GraphQLError

// Newline characters may not be recognized by server
fun Query.createRequest() = "{\"query\": \"${render().escape()}\"}"
    .flatten()

private fun String.escape() = replace("\"", "\\\"")
