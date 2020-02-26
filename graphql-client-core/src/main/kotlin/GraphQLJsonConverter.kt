interface GraphQLJsonConverter {
    fun <T> deserialize(jsonString: String, classInfo : Class<T>): T
}