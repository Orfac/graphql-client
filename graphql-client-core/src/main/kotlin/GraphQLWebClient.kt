interface GraphQLWebClient {
    fun send(url : String, body: String): String
}