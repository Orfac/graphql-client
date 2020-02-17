package software.graphql.client

fun query(init: Query.() -> Unit) = Query().apply(init)

class Query : Field("query") {
    fun version() = initScalarField("version")

    fun person(name: String? = null, init: Person.() -> Unit) =
        initField(
            Person("person", Argument("name", name)),
            init
        )

    fun book(maxPages: Int? = 200, authorName: String? = null, init: Book.() -> Unit) =
        initField(
            Book("book", Argument("maxPages", maxPages, 200), Argument("authorName", authorName)),
            init
        )

    fun allBooks(authorNames: List<String> = emptyList(), init: Book.() -> Unit) =
        initField(
            Book("allBooks", Argument("authorNames", authorNames, emptyList())),
            init
        )
}

class Person(fieldName: String, vararg arguments: Argument<*>) : Field(fieldName, *arguments) {
    fun name() = initScalarField("name")
    fun age() = initScalarField("age")
}

class Book(fieldName: String, vararg arguments: Argument<*>) : Field(fieldName, *arguments) {
    fun title() = initScalarField("title")
    fun author(init: Person.() -> Unit) = initField(Person("author"), init)
}