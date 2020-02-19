package software.graphql.client.validation

import org.junit.Test
import software.graphql.client.newFragment
import software.graphql.client.query
import kotlin.test.assertFails

internal class NameValidationTest {
    @Test
    fun `bad scalar field name`() {
        assertFails {
            query { `fun` { name() } }.render()
        }
        assertFails {
            query { `fun` { numberName() } }.render()
        }
        assertFails {
            query { `fun` { ageRecommendation() } }.render()
        }
        assertFails {
            query { `fun` { isAvailable() } }.render()
        }
    }

    @Test
    fun `bad scalar field alias`() {
        assertFails {
            query {
                `fun` { normalScalarField(alias = "bad alias") }.render()
            }
        }
    }

    @Test
    fun `bad complex field name`() {
        assertFails {
            query {
                `bad fun` { normalScalarField() }
            }.render()
        }
    }

    @Test
    fun `bad argument name`() {
        assertFails {
            query { `fun` { badArgument(100) } }.render()
        }
    }

    @Test
    fun `bad fragment name`() {
        assertFails {
            newFragment("a very bad fragment!", ::Fun) {
            }(query {}).render()
        }
    }
}