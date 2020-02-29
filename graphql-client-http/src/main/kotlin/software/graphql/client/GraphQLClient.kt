package software.graphql.client

class GraphQLClient(private val httpSender: HttpSender, private val jsonReader: JsonObjectReader) {
    private var uri: String = "http://127.0.0.1"

    fun uri(uri: String): GraphQLClient {
        this.uri = uri
        return this
    }

    // Query as argument, because it's GraphQLClient, not just Http
    fun <T : Any> sendQuery(query: Query, dataType: TypeResolver<GraphQLResponse<T>>): Callback<GraphQLResponse<T>> = {
        jsonReader.readObject(
            httpSender.send(uri, query.createRequest())(),
            dataType
        )
    }
}

typealias Callback<T> = () -> T

fun <T : Any?> Callback<T>.call() = this()

data class GraphQLResponse<T>(
    val data: T? = null,
    val errors: Collection<PlainTextGraphQLError>? = null,
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
