package software.graphql.client

import com.fasterxml.jackson.databind.ObjectMapper

object JacksonObjectReader : JsonObjectReader {
    private val mapper: ObjectMapper = ObjectMapper().findAndRegisterModules()

    override fun <T : Any> readObject(json: String, valueType: Class<out T>): T =
        mapper.readValue(json, valueType)
}