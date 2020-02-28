import com.fasterxml.jackson.databind.ObjectMapper
import core.GraphQLJsonConverter
import reactor.core.publisher.Mono


class DefaultGraphQLJsonConverter : GraphQLJsonConverter {
    val mapper: ObjectMapper = ObjectMapper().findAndRegisterModules()

    override fun <T> deserialize(jsonString: String, classInfo: Class<T>): T {
        return mapper.readValue(jsonString, classInfo)
    }

    override fun <T> deserializeReactive(jsonString: Mono<String>, classInfo: Class<T>) =
        jsonString.map { deserialize(it, classInfo) }
}
