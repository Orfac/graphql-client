package software.graphql.client

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object GsonObjectReader : JsonObjectReader {
    private val gson = Gson()

    override fun <T : Any> readObject(json: String, valueType: TypeResolver<T>): T =
        gson.fromJson(json, (valueType as GsonTypeResolver<T>).typeToken.type)
}

inline fun <reified T> gsonType(): GsonTypeResolver<T> =
    GsonTypeResolver(object : TypeToken<T>() {})

class GsonTypeResolver<T>(val typeToken: TypeToken<T>) : TypeResolver<T>