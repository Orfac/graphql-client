package software.graphql.client

import com.google.gson.Gson

object GsonObjectReader : JsonObjectReader {
    override fun <T> readObject(json: String, valueType: Class<T>): T =
        Gson().fromJson(json, valueType)
}