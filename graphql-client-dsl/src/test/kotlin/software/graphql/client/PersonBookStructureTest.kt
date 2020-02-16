package software.graphql.client

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

internal class PersonBookStructureTest {
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
        }

        assertEquals(Render.STRUCTURE.SIMPLEST_QUERY, query.render().flatten())
        assertEquals(ToString.STRUCTURE.SIMPLEST_QUERY, query.toString().flatten())
    }

    @Test
    fun `nested fields`() {
        val query = query {
            book {
                author {
                    name()
                }
            }
        }

        assertEquals(Render.STRUCTURE.NESTED_FIELDS, query.render().flatten())
        assertEquals(ToString.STRUCTURE.NESTED_FIELDS, query.toString().flatten())
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
            version()
        }

        assertEquals(Render.STRUCTURE.COMPLEX_QUERY, query.render().flatten())
        assertEquals(ToString.STRUCTURE.COMPLEX_QUERY, query.toString().flatten())
    }
}