package software.graphql.client.features

import org.junit.Test
import software.graphql.client.example.allBooks
import software.graphql.client.example.book
import software.graphql.client.example.person
import software.graphql.client.flatten
import software.graphql.client.getTestResource
import software.graphql.client.query
import kotlin.test.assertEquals

internal class ArgumentsTest {
    @Test
    fun `int argument`() {
        val query = query {
            book(maxPages = 100) {
                title()
            }
        }

        assertEquals(getLocalTestResource("intArgument"), query.render().flatten())
    }

    @Test
    fun `string argument`() {
        val query = query {
            person(name = "Boris") {
                age()
            }
        }

        assertEquals(getLocalTestResource("stringArgument"), query.render().flatten())
    }

    @Test
    fun `default argument not printed`() {
        val query = query {
            book(maxPages = 200) {
                title()
            }
        }

        assertEquals(getLocalTestResource("defaultArgumentNotPrinted"), query.render().flatten())
    }

    @Test
    fun `multiple arguments`() {
        val query = query {
            book(maxPages = 50, authorName = "Boris") {
                title()
            }
        }

        assertEquals(getLocalTestResource("multipleArguments"), query.render().flatten())
    }

    @Test
    fun `list as argument`() {
        val query = query {
            allBooks(authorNames = listOf("Boris", "Arseniy")) {
                author {
                    name()
                }
                title()
            }
        }

        assertEquals(getLocalTestResource("listAsArgument"), query.render().flatten())
    }

    private fun getLocalTestResource(fileName: String) =
        getTestResource("/render/arguments/$fileName")
}