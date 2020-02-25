interface GraphQLJsonConverter {
    fun <T> deserialize(jsonString: String): T
}