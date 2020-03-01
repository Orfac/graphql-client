package software.graphql.client

typealias Callback<T> = () -> T

fun <T : Any?> Callback<T>.call() = this()