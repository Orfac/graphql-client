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

        assertEquals(getLocalTestResource("simplestQuery"), query.render().flatten())
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

        assertEquals(getLocalTestResource("nestedFields"), query.render().flatten())
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

        assertEquals(getLocalTestResource("complexQuery"), query.render().flatten())
    }

    private fun getLocalTestResource(fileName: String) =
        getTestResource("/render/structure/$fileName")
}