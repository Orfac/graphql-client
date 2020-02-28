package software.graphql.client

import com.google.gson.Gson

object GsonObjectReader : JsonObjectReader {
    private val gson = Gson()

    override fun <T : Any> readObject(json: String, valueType: Class<out T>): T? =
        gson.fromJson(json, valueType)
}