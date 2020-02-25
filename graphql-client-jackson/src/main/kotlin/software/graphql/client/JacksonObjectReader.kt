package software.graphql.client

import com.fasterxml.jackson.databind.ObjectMapper

object JacksonObjectReader : JsonObjectReader {
    override fun <T> readObject(json: String, valueType: Class<T>): T =
        ObjectMapper().readValue(json, valueType)
}