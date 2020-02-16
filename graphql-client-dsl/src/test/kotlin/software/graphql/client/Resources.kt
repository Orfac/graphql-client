package software.graphql.client

object Render {
    object STRUCTURE {
        const val SIMPLEST_QUERY = "query { version }"
        const val NESTED_FIELDS = "query { book { author { name } } }"
        const val COMPLEX_QUERY = "query { book { title author { name } } person { name age } version }"
    }

    object ARGUMENTS {
        const val INT_ARGUMENT = "query { book(maxPages: 100) { title } }"
        const val STRING_ARGUMENT = "query { person(name: \"Boris\") { age } }"
        const val DEFAULT_ARGUMENT_NOT_PRINTED = "query { book { title } }"
        const val MULTIPLE_ARGUMENTS = "query { book(maxPages: 50, authorName: \"Boris\") { title } }"
        const val LIST_AS_ARGUMENT =
            "query { allBooks(authorNames: [\"Boris\", \"Arseniy\"]) { author { name } title } }"
    }
}

object ToString {
    object STRUCTURE {
        const val SIMPLEST_QUERY = "query { version }"
        const val NESTED_FIELDS = "query { book {...} }"
        const val COMPLEX_QUERY = "query { book {...} person {...} version }"
    }

    object ARGUMENTS {
        const val INT_ARGUMENT = "query { book(maxPages: 100) {...} }"
        const val STRING_ARGUMENT = "query { person(name: \"Boris\") {...} }"
        const val DEFAULT_ARGUMENT_NOT_PRINTED = "query { book {...} }"
        const val MULTIPLE_ARGUMENTS = "query { book(maxPages: 50, authorName: \"Boris\") {...} }"
        const val LIST_AS_ARGUMENT = "query { allBooks(authorNames: [\"Boris\", \"Arseniy\"]) {...} }"
    }
}
