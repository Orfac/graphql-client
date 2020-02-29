package software.graphql.client

interface JsonObjectReader {
    fun <T : Any> readObject(json: String, valueType: TypeResolver<T>): T
}

interface TypeResolver<T>