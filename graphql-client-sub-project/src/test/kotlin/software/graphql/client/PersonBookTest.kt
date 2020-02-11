package software.graphql.client

import org.junit.Test
import java.time.LocalDate
import kotlin.test.assertEquals

/**
 * This class builds a query for schema like this:
 * <code>
 *     type Query {
 *         schemaVersion
 *         person(name: String!, dateOfBirth: MyDateType, favouritePets: [Pet!]! = []): Person
 *         book(authorName: String = "Boris", title: String): Book
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
internal class PersonBookTest {
    private fun Query.person(
        name: String,
        dateOfBirth: MyDateType? = null,
        favouritePets: List<Pet> = emptyList(),
        init: Person.() -> Unit
    ) = initRoot(
        "person", Person(),
        Argument("name", name),
        Argument("dateOfBirth", dateOfBirth, null),
        Argument("favouritePet", favouritePets, null),
        init = init
    )

    private fun Query.book(authorName: String? = "Boris", title: String? = null, init: Book.() -> Unit) =
        initRoot(
            "book", Book(),
            Argument("authorName", authorName, "Boris"),
            Argument("title", title, null),
            init = init
        )

    private fun Query.allBooks(
        authorNames: List<String> = emptyList(), titles: List<String> = emptyList(), init: Book.() -> Unit
    ) = initRoot(
        "allBooks", Book(),
        Argument("authorNames", authorNames, emptyList<String>()),
        Argument("titles", titles, emptyList<String>()),
        init = init
    )

    private fun Query.schemaVersion() = initRoot("schemaVersion", ScalarField())

    class Person : Field() {
        fun book(title: String, init: Book.() -> Unit) =
            initField("book", Book(), Argument("title", title), init = init)

        fun name(capitalize: Boolean = false) =
            initField("name", ScalarField(), Argument("capitalize", capitalize, false))

        fun age() = initField("age", ScalarField())
    }

    class Book : Field() {
        fun author(init: Person.() -> Unit) = initField("author", Person(), init = init)
        fun title() = initField("title", ScalarField())
    }

    class MyDateType(private val date: LocalDate) {
        override fun toString() = "\"$date\""
    }

    enum class Pet {
        CAT, DOG
    }

    @Test
    fun test() {
        val query = query {
            schemaVersion()
            person(
                name = "Boris",
                dateOfBirth = MyDateType(LocalDate.of(1970, 1, 1)),
                favouritePets = arrayListOf(Pet.CAT, Pet.DOG)
            ) {
                name()
                age()
                book(title = "This tool spec") {
                    author {
                        // argument will not be present in the query, because 'false' is the default value
                        name(capitalize = false)
                    }
                    title()
                }
            }
            // defaults with authorName equal to "Boris"
            book(title = "This tool spec") {
                title()
            }
            // null will be in the query, because it's not default
            book(authorName = null, title = "Hello world") {
                author {
                    name(capitalize = true)
                }
            }
            allBooks(titles = arrayListOf("Hello world", "This tool spec")) {
                author {
                    name()
                }
            }
        }
        // 'flattenQuery' leaves only words, numbers and double quotes to make testing easier
        assertEquals(
            flattenQuery(query.toString()),
            "query schemaVersion person name \"Boris\" dateOfBirth \"1970 01 01\" favouritePet CAT DOG name age " +
                    "book title \"This tool spec\" author name title book title \"This tool spec\" title " +
                    "book authorName null title \"Hello world\" author name capitalize true " +
                    "allBooks titles \"Hello world\" \"This tool spec\" author name"
        )
    }
}