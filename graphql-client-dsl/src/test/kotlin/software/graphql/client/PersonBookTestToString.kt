package software.graphql.client

import org.junit.Test
import kotlin.test.assertEquals

internal class PersonBookTestToString {

    @Test
    fun `simplest query`() {
        val query = query {
            version()
        }.toString()

        assertEquals(ToString.SIMPLEST_QUERY, query.flatten())
    }

    @Test
    fun `scalar field`() {
        query {
            val version = version()
            assertEquals(ToString.SCALAR_FIELD, version.toString())
        }
    }

    @Test
    fun `nested fields`() {
        val query = query {
            book {
                author {
                    name()
                }
            }
        }.toString()

        assertEquals(ToString.NESTED_FIELDS, query.flatten())
    }

    @Test
    fun `complex query`() {
        val query = query {
            book {
                author {
                    name()
                }
                title()
            }
            person {
                name()
                age()
            }
            version()
        }.toString()

        assertEquals(ToString.COMPLEX_QUERY, query.flatten())
    }
}

