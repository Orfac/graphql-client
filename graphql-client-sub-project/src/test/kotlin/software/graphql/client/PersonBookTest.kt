package software.graphql.client

import org.junit.Test
import java.time.LocalDate
import kotlin.test.assertEquals

/**
 * This class builds a query for schema like this:
 * <code>
 *     type Query {
 *         schemaVersion
 *         person(name: String!, dateOfBirth: MyDateType, favouritePet: Pet = null): Person
 *         book(authorName: String = "Boris", title: String): Book
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
        favouritePet: Pet? = null,
        init: Person.() -> Unit
    ) = initRoot(
        "person",
        Person(),
        Argument("name", name),
        Argument("dateOfBirth", dateOfBirth, null),
        Argument("favouritePet", favouritePet, null),
        init = init
    )

    private fun Query.book(authorName: String? = "Boris", title: String? = null, init: Book.() -> Unit) =
        initRoot(
            "book", Book(),
            Argument("authorName", authorName, "Boris"),
            Argument("title", title, null),
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

    @Suppress("unused")
    enum class Pet {
        CAT, DOG
    }

    @Test
    fun test() {
        val query = query {
            schemaVersion()
            person(name = "Boris", dateOfBirth = MyDateType(LocalDate.of(1970, 1, 1)), favouritePet = Pet.CAT) {
                name(capitalize = true)
                age()
                book(title = "This tool spec") {
                    author {
                        // will not be present in the query, because 'false' is the default value
                        name(capitalize = false)
                    }
                    title()
                }
            }
            // defaults with authorName equal to "Boris"
            book(title = "This tool spec") {
                title()
                author {
                    name()
                    age()
                }
            }
            // null will be in the query, because it's not default
            book(authorName = null, title = "Hello world") {
                author {
                    name()
                }
            }
        }
        // 'flattenQuery' leaves only words and numbers to make testing easier
        assertEquals(
            flattenQuery(query.toString()),
            "query schemaVersion person name Boris dateOfBirth 1970 01 01 favouritePet CAT name capitalize true age " +
                    "book title This tool spec author name title book title This tool spec title author name age " +
                    "book authorName null title Hello world author name"
        )
    }
}