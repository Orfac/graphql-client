package software.graphql.client

import java.time.LocalDate

/**
 * This is user's code to build queries for schema like this:
 * <code>
 *     type Query {
 *         schemaVersion
 *         person(name: String!, dateOfBirth: MyDateType, favouritePets: [Pet!]! = []): Person
 *         book(authorName: String = "Boris", title: String): Book
 *         authorBook(dateOfBirth: MyDateType, favouritePets: [Pet!]! = [],
 *                      authorName: String = "Boris", title: String): AuthorBook
 *         allBooks(authorNames: [String!] = [], titles: [String!]!): [Book]
 *     }
 *     type Person {
 *         book(title: String!): Book
 *         name(capitalize: Boolean! = false)
 *         age
 *     }
 *     type Book {
 *         author: Person
 *         title: String
 *     }
 *     enum Pet {
 *         CAT, DOG
 *     }
 * </code>
 */

internal fun Query.person(
    name: String,
    dateOfBirth: MyDateType? = null,
    favouritePets: List<Pet> = emptyList(),
    init: Person.() -> Unit
) = initRoot(
    "person", Person(),
    Argument("name", name),
    Argument("dateOfBirth", dateOfBirth, null),
    Argument("favouritePet", favouritePets, emptyList<Pet>()),
    init = init
)

internal fun Query.book(authorName: String? = "Boris", title: String? = null, init: Book.() -> Unit) =
    initRoot(
        "book", Book(),
        Argument("authorName", authorName, "Boris"),
        Argument("title", title, null),
        init = init
    )

internal fun Query.allBooks(
    authorNames: List<String> = emptyList(), titles: List<String> = emptyList(), init: Book.() -> Unit
) = initRoot(
    "allBooks", Book(),
    Argument("authorNames", authorNames, emptyList<String>()),
    Argument("titles", titles, emptyList<String>()),
    init = init
)

internal fun Query.schemaVersion() = initRoot("schemaVersion", ScalarField())

internal class Person : Field() {
    fun book(title: String, init: Book.() -> Unit) =
        initField("book", Book(), Argument("title", title), init = init)

    fun name(capitalize: Boolean = false) =
        initField("name", ScalarField(), Argument("capitalize", capitalize, false))

    fun age() = initField("age", ScalarField())
}

internal class Book : Field() {
    fun author(init: Person.() -> Unit) = initField("author", Person(), init = init)
    fun title() = initField("title", ScalarField())
}

internal class MyDateType(private val date: LocalDate) {
    override fun toString() = "\"$date\""
}

internal enum class Pet {
    CAT, DOG
}