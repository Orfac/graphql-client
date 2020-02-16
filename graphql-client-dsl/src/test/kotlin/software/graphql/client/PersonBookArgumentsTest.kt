package software.graphql.client

import org.junit.Test
import kotlin.test.assertEquals

internal class PersonBookArgumentsTest {
    @Test
    fun `int argument`() {
        val query = query {
            book(maxPages = 100) {
                title()
            }
        }

        assertEquals(Render.ARGUMENTS.INT_ARGUMENT, query.render().flatten())
        assertEquals(ToString.ARGUMENTS.INT_ARGUMENT, query.toString().flatten())
    }

    @Test
    fun `string argument`() {
        val query = query {
            person(name = "Boris") {
                age()
            }
        }

        assertEquals(Render.ARGUMENTS.STRING_ARGUMENT, query.render().flatten())
        assertEquals(ToString.ARGUMENTS.STRING_ARGUMENT, query.toString().flatten())
    }

    @Test
    fun `default argument not printed`() {
        val query = query {
            book(maxPages = 200) {
                title()
            }
        }

        assertEquals(Render.ARGUMENTS.DEFAULT_ARGUMENT_NOT_PRINTED, query.render().flatten())
        assertEquals(ToString.ARGUMENTS.DEFAULT_ARGUMENT_NOT_PRINTED, query.toString().flatten())
    }

    @Test
    fun `multiple arguments`() {
        val query = query {
            book(maxPages = 50, authorName = "Boris") {
                title()
            }
        }

        assertEquals(Render.ARGUMENTS.MULTIPLE_ARGUMENTS, query.render().flatten())
        assertEquals(ToString.ARGUMENTS.MULTIPLE_ARGUMENTS, query.toString().flatten())
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

        assertEquals(Render.ARGUMENTS.LIST_AS_ARGUMENT, query.render().flatten())
        assertEquals(ToString.ARGUMENTS.LIST_AS_ARGUMENT, query.toString().flatten())
    }
}