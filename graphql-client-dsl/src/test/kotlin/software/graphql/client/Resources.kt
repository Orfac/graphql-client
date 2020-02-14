package software.graphql.client

object Render {
    const val SIMPLEST_QUERY = "query { version }"
    const val NESTED_FIELDS = "query { book { author { name } } }"
    const val COMPLEX_QUERY = "query { book { title author { name } } person { name age } }"
}

object ToString {
    const val SIMPLEST_QUERY = "query { version }"
    const val SCALAR_FIELD = "version"
    const val NESTED_FIELDS = "query { book {...} }"
    const val COMPLEX_QUERY = "query { book {...} person {...} version }"
}
