interface GraphQLWebClient {
    fun send(url : String, request: String): String
}