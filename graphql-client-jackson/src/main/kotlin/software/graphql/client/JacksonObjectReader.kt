package software.graphql.client

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper

object JacksonObjectReader : JsonObjectReader {
    private val mapper: ObjectMapper = ObjectMapper().findAndRegisterModules()

    override fun <T : Any> readObject(json: String, valueType: TypeResolver<T>): T =
        mapper.readValue(json, (valueType as JacksonTypeResolver<T>).typeReference)
}

inline fun <reified T> jacksonType(): JacksonTypeResolver<T> =
    JacksonTypeResolver(object : TypeReference<T>() {})

class JacksonTypeResolver<T>(val typeReference: TypeReference<T>) : TypeResolver<T>