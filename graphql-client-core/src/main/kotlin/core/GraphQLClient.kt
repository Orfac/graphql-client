package core

open class GraphQLClient(
    val url: String,
    var webClient: GraphQLWebClient,
    var jsonConverter: GraphQLJsonConverter
) {
    inline fun <reified T> sendQuery(query: String): T {
        val response = webClient.send(url, query)
        return jsonConverter.deserialize(response, T::class.java)
    }
}