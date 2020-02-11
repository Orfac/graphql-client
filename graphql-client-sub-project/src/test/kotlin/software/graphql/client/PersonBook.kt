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
 *     type AuthorBook = Person | Book
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

internal fun Query.authorBook(
    dateOfBirth: MyDateType? = null,
    favouritePets: List<Pet> = emptyList(),
    authorName: String? = "Boris",
    title: String? = null,
    init: AuthorBook.() -> Unit
) = initRoot(
    "authorBook", AuthorBook(),
    Argument("dateOfBirth", dateOfBirth, null),
    Argument("favouritePet", favouritePets, emptyList<Pet>()),
    Argument("authorName", authorName, "Boris"),
    Argument("title", title, null),
    init = init
)

internal fun Query.schemaVersion() = initRoot("schemaVersion", ScalarField())

internal interface IPerson {
    fun book(title: String, init: Book.() -> Unit): Book
    fun name(capitalize: Boolean = false): ScalarField
    fun age(): ScalarField
}

internal interface IBook {
    fun author(init: Person.() -> Unit): Person
    fun title(): ScalarField
}

internal class Person : Field(), IPerson {
    override fun book(title: String, init: Book.() -> Unit) =
        initField("book", Book(), Argument("title", title), init = init)

    override fun name(capitalize: Boolean) =
        initField("name", ScalarField(), Argument("capitalize", capitalize, false))

    override fun age() = initField("age", ScalarField())
}

internal class Book : Field(), IBook {
    override fun author(init: Person.() -> Unit) = initField("author", Person(), init = init)
    override fun title() = initField("title", ScalarField())
}

internal class AuthorBook : Field(), IPerson by Person(), IBook by Book()

internal class MyDateType(private val date: LocalDate) {
    override fun toString() = "\"$date\""
}

internal enum class Pet {
    CAT, DOG
}