package software.graphql.client

data class GraphQLResponse<T>(
    val data: T? = null,
    val errors: Collection<PlainTextGraphQLError>? = null,
    val extensions: Any? = null
)

interface GraphQLError {
    val message: String
}

data class PlainTextGraphQLError(override val message: String) : GraphQLError
