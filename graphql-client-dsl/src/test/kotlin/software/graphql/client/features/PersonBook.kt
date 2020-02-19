package software.graphql.client.features

import software.graphql.client.*

fun Query.version(alias: String = "") = this.initScalarField("version", alias)

fun Query.person(name: String? = null, alias: String = "", init: Person.() -> Unit) =
    initField(
        Person(
            "person",
            alias,
            listOf(Argument("name", name))
        ),
        init
    )

fun Query.book(maxPages: Int? = 200, authorName: String? = null, alias: String = "", init: Book.() -> Unit) =
    initField(
        Book(
            "book",
            alias,
            listOf(
                Argument("maxPages", maxPages, 200),
                Argument("authorName", authorName)
            )
        ),
        init
    )

fun Query.allBooks(authorNames: List<String> = emptyList(), alias: String = "", init: Book.() -> Unit) =
    initField(
        Book(
            "allBooks",
            alias,
            listOf(Argument("authorNames", authorNames, emptyList()))
        ),
        init
    )

class Person(fieldName: String, alias: String, arguments: Collection<Argument<*>> = emptyList()) :
    Field(fieldName, alias, arguments) {

    fun name(alias: String = "") = initScalarField("name", alias)
    fun age(alias: String = "") = initScalarField("age", alias)
}

class Book(fieldName: String, alias: String, arguments: Collection<Argument<*>> = emptyList()) :
    Field(fieldName, alias, arguments) {

    fun title(alias: String = "") = initScalarField("title", alias)
    fun author(alias: String = "", init: Person.() -> Unit) =
        initField(Person("author", alias), init)
}