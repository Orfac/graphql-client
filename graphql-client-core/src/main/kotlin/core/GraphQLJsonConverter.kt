package core

import reactor.core.publisher.Mono

interface GraphQLJsonConverter {
    fun <T> deserialize(jsonString: String, classInfo : Class<T>): T
    fun <T> deserializeReactive(jsonString: Mono<String>, classInfo: Class<T>): Mono<T>
}