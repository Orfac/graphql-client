package software.graphql.client

fun query(init: Query.() -> Unit) = Query().apply(init)

class Query : Field("query") {
    fun version() = initScalarField("version")

    fun person(init: Person.() -> Unit) = initField(Person("person"), init)
    fun book(init: Book.() -> Unit) = initField(Book("book"), init)
}

class Person(fieldName: String) : Field(fieldName) {
    fun name() = initScalarField("name")
    fun age() = initScalarField("age")
}

class Book(fieldName: String) : Field(fieldName) {
    fun title() = initScalarField("title")
    fun author(init: Person.() -> Unit) = initField(Person("author"), init)
}