package software.graphql.client

import reactor.core.publisher.Mono

internal const val NO_ID = -1

internal class NettyCallback<T>(internal val id: Int, private val callback: Callback<T>) : Callback<T> {
    override fun invoke() = callback()
}

internal val responses = mutableListOf<Mono<out Any?>>()
internal fun <T> MutableCollection<T>.addLast(element: T) = synchronized(this) {
    size.also {
        add(element)
    }
}

fun <T : Any?> Callback<T>.asMono(): Mono<T> =
    Mono.fromCallable(this::call)
        .let {
            if (this is NettyCallback<T> && id != NO_ID) {
                @Suppress("UNCHECKED_CAST")
                responses[id] as? Mono<T> ?: it
            } else it
        }