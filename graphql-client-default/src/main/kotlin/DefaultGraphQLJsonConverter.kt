import com.fasterxml.jackson.databind.ObjectMapper
import core.GraphQLJsonConverter

class DefaultGraphQLJsonConverter : GraphQLJsonConverter {
    override fun <T> deserialize(jsonString: String, classInfo: Class<T>): T {
        return ObjectMapper().readValue(jsonString, classInfo)
    }
}