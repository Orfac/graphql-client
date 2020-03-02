package software.graphql.client

data class GraphQLResponse<D : Any, E : GraphQLError>(
    val data: D? = null,
    val errors: Collection<E>? = null,
    val extensions: Any? = null
)

interface GraphQLError {
    val message: String
}

data class PlainTextGraphQLError(override val message: String) : GraphQLError
