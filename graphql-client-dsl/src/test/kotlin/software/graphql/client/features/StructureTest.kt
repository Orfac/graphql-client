package software.graphql.client.features

import org.junit.Test
import software.graphql.client.flatten
import software.graphql.client.getTestResource
import software.graphql.client.query
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertNotNull

internal class StructureTest {
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
    fun `throws an exception when contains subfields with same name and alias`() {
        assertFails {
            query {
                book(alias = "book") { title() }
                book(alias = "book") { author { name() } }
            }.render()
        }
        assertFails {
            query {
                book { title() }
                book { author { name() } }
            }.render()
        }

        assertNotNull(
            kotlin.runCatching {
                query {
                    book { title() }
                    book(alias = "book") { author { name() } }
                }.render()
            }.getOrNull()
        )
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