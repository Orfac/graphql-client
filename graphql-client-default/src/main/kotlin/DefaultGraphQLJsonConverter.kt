import core.GraphQLJsonConverter

class DefaultGraphQLJsonConverter : GraphQLJsonConverter {
    override fun <T> deserialize(jsonString: String, classInfo: Class<T>): T {
        val objectMapper = ObjectMapper()
    }
}