package software.graphql.client

import reactor.core.publisher.Mono

internal class NettyCallback<T>(internal val id: Int, private val callback: Callback<T>) : Callback<T> {
    override fun invoke() = callback()
}

internal val responses = mutableListOf<Mono<out Any?>>()
internal fun <T> MutableCollection<T>.addLast(element: T) = synchronized(this) {
    size.also {
        add(element)
    }
}

/**
 * This method tries to restore the Mono used for creating this Callback (if it was created this way).
 * If it fails, it wraps the callback via [Mono.fromCallable]
 */
fun <T : Any?> Callback<T>.asMono(): Mono<T> =
    Mono.fromCallable(this::call)
        .let {
            if (this is NettyCallback<T>) {
                @Suppress("UNCHECKED_CAST")
                responses[id] as? Mono<T> ?: it
            } else it
        }