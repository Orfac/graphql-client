package software.graphql.client

interface JsonObjectReader {
    fun <T> readObject(json: String, valueType: Class<T>): T
}