class GraphQLClient(
    url: String,
    public var webClient: GraphQLWebClient,
    public var jsonConverter: GraphQLJsonConverter
) {
    inline fun <reified T> sendQuery(query: String): T {
        val response = webClient.send(query)
        return jsonConverter.deserialize(response, T::class.java)
    }
}