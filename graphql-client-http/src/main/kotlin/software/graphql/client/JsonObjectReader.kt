package software.graphql.client

interface JsonObjectReader {
    /**
     * Should construct an object of type T or throw an exception
     */
    fun <T : Any> readObject(json: String, valueType: TypeResolver<T>): T
}

/**
 * Classes implementing this interface should contain information about the constructed type, specific to the
 * JSON tool you use.
 *
 * Consider these examples:
 * * software.graphql.client.GsonTypeResolver in the module graphql-client-gson,
 * * software.graphql.client.JacksonTypeResolver in the module graphql-client-jackson.
 */
interface TypeResolver<T>