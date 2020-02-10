package software.graphql.client

import org.junit.Test
import kotlin.test.assertEquals

/**
 * This class defines following schema (it does not really define types of scalar fields):
 * <code>
 *     type Query {
 *         person(name: String!, age: Integer): Person
 *         book(authorName: String, title: String): Book
 *     }
 *     type Person {
 *         book(title: String!): Book
 *         name(capitalize: Boolean! = false): String
 *         age: Integer
 *     }
 *     type Book {
 *         author: Person
 *         title: String
 *     }
 * </code>
 */
internal class PersonBookTest {
    fun Query.person(name: String, age: Int? = null, init: Person.() -> Unit) =
        initRoot(Person(name, age), init)

    fun Query.book(authorName: String? = null, title: String? = null, init: Book.() -> Unit) =
        initRoot(Book(authorName = authorName, title = title), init)

    class Person(private val name: String? = null, private val age: Int? = null) : Type("Person") {
        fun book(title: String, init: Book.() -> Unit) = initField(Book(title = title), init)
        fun name(capitalize: Boolean = false) = initField(PersonName(capitalize))
        fun age() = initField(PersonAge())

        private class PersonName(val capitalize: Boolean = false) : ScalarType("name")
        private class PersonAge : ScalarType("age")
    }

    class Book(private val authorName: String? = null, private val title: String? = null) : Type("Book") {
        fun author(init: Person.() -> Unit) = initField(Person(), init)
        fun title() = initField(BookTitle())

        private class BookTitle : ScalarType("title")
    }

    @Test
    fun test() {
        val query = query {
            person(name = "Boris", age = 19) {
                name(capitalize = true)
                age()
                book(title = "This tool spec") {
                    author {
                        name()
                    }
                    title()
                }
            }
            book(authorName = "Boris") {
                title()
                author {
                    name()
                    age()
                }
            }
        }
        assertEquals(
            flattenQuery(query.toString()).replace(" capitalize false", ""),
            "query Person name Boris age 19 name capitalize true age Book title This tool " +
                    "spec Person name title Book authorName Boris title Person name age"
        )
    }
}