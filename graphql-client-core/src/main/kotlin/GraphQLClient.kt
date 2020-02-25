class GraphQLClient(
    url: String,
    private var webClient: GraphQLWebClient,
    private var jsonConverter: GraphQLJsonConverter
) {
    fun setWebClient(webClient: GraphQLWebClient) {
        this.webClient = webClient
    }

    fun setJsonConverter(jsonConverter: GraphQLJsonConverter) {
        this.jsonConverter = jsonConverter
    }

    fun <T> sendQuery(query: String): T {
        val response = webClient.send(query)
        return jsonConverter.deserialize(response)
    }
}