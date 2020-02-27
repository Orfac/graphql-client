package software.graphql.client.structureArguments

import software.graphql.client.Argument
import software.graphql.client.Field
import software.graphql.client.initScalarField

fun query(queryName: String = "", init: Query.() -> Unit) = Query(queryName).apply(init)

class Query(queryName: String) : Field("query $queryName".trim(), "") {
    fun version(alias: String = "") = this.initScalarField("version", alias)

    fun person(name: String? = null, alias: String = "", init: Person.() -> Unit) =
        initField(
            Person(
                "person",
                alias,
                Argument("name", name)
            ),
            init
        )

    fun book(maxPages: Int? = 200, authorName: String? = null, alias: String = "", init: Book.() -> Unit) =
        initField(
            Book(
                "book",
                alias,
                Argument("maxPages", maxPages, 200),
                Argument("authorName", authorName)
            ),
            init
        )

    fun allBooks(authorNames: List<String> = emptyList(), alias: String = "", init: Book.() -> Unit) =
        initField(
            Book(
                "allBooks",
                alias,
                Argument("authorNames", authorNames, emptyList())
            ),
            init
        )
}

class Person(fieldName: String, alias: String, vararg arguments: Argument<*>) :
    Field(fieldName, alias, *arguments) {

    fun name(alias: String = "") = initScalarField("name", alias)
    fun age(alias: String = "") = initScalarField("age", alias)
}

class Book(fieldName: String, alias: String, vararg arguments: Argument<*>) :
    Field(fieldName, alias, *arguments) {

    fun title(alias: String = "") = initScalarField("title", alias)
    fun author(alias: String = "", init: Person.() -> Unit) =
        initField(Person("author", alias), init)
}