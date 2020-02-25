package software.graphql.client.features

import org.junit.Test
import software.graphql.client.*
import kotlin.test.assertEquals
import kotlin.test.assertFails

class FragmentsTest {
    @Test
    fun `throws an exception when no subfields specified`() {
        assertFails {
            newFragment("doesntMatter", ::Person) {
            }(query {}).render()
        }
        assertFails {
            query {
                person {
                    addInlineFragment(::Person) {}
                }
            }.render()
        }
    }

    @Test
    fun `named fragment`() {
        val personInfo =
            newFragment("personInfo", ::Person) {
                name()
                age()
            }

        val query = query {
            person {
                addFragment(personInfo(this@query))
            }
        }

        assertEquals(getLocalTestResource("namedFragment"), query.render().flatten())
    }

    @Test
    fun `inline fragment`() {
        val query = query {
            person {
                addInlineFragment(::Person) {
                    name()
                    age()
                }
            }
        }

        assertEquals(getLocalTestResource("inlineFragment"), query.render().flatten())
    }

    @Test
    fun `multiple inline fragments`() {
        val query = query {
            person {
                addInlineFragment(::Person) {
                    name()
                }
                addInlineFragment(::Person) {
                    name()
                }
            }
        }

        assertEquals(getLocalTestResource("multipleInlineFragments"), query.render().flatten())
    }

    @Test
    fun `multiple equal fragments declared once`() {
        val personName = newFragment("personName", ::Person) {
            name()
        }

        val query = query {
            person {
                addFragment(personName(this@query))
                addFragment(personName(this@query))
            }
        }

        assertEquals(getLocalTestResource("multipleEqualFragmentsDeclaredOnce"), query.render().flatten())
    }

    @Test
    fun `nested inline fragments`() {
        val query = query {
            person {
                addInlineFragment(::Person) {
                    name()
                    addInlineFragment(::Person) {
                        age()
                    }
                }
            }
        }

        assertEquals(getLocalTestResource("nestedInlineFragments"), query.render().flatten())
    }

    @Test
    fun `nested mixed fragments`() {
        val personName = newFragment("personName", ::Person) {
            name()
        }

        val query = query {
            person {
                addFragment(personName(this@query))
                addInlineFragment(::Person) {
                    age()
                }
            }
        }

        assertEquals(getLocalTestResource("nestedMixedFragments"), query.render().flatten())
    }

    private fun getLocalTestResource(fileName: String) =
        getTestResource("/render/fragments/$fileName")
}