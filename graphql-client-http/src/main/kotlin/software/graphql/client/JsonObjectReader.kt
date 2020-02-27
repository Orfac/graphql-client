package software.graphql.client

interface JsonObjectReader {
    fun <T : Any> readObject(json: String, valueType: Class<out T>): T?
}