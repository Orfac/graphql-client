package software.graphql.client

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

internal class PersonBookTestRender {

    @Test
    fun `throws an exception when no subfields specified`() {
        assertFails {
            query { }.render()
        }
        assertFails {
            query { book { } }.render()
        }
        assertFails {
            query { person { } }.render()
        }
        assertFails {
            query { book { author { } } }.render()
        }

    }

    @Test
    fun `simplest query`() {
        val query = query {
            version()
        }.render()

        assertEquals(Render.SIMPLEST_QUERY, query.flatten())

    }

    @Test
    fun `nested fields`() {
        val query = query {
            book {
                author {
                    name()
                }
            }
        }.render()

        assertEquals(Render.NESTED_FIELDS, query.flatten())

    }

    @Test
    fun `complex query`() {
        val query = query {
            book {
                title()
                author {
                    name()
                }
            }
            person {
                name()
                age()
            }
        }.render()

        assertEquals(Render.COMPLEX_QUERY, query.flatten())

    }
}