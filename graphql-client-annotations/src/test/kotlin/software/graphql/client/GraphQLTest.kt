package software.graphql.client

import org.junit.Test
import kotlin.test.assertEquals

class GraphQLTest {


    @Test
    fun `building query works for simple class with 2 properties`() {
        data class product(val id: Int, val name: String)

        assertEquals(
            "product {\n\tid\n\tname\n}",
            GraphQL.buildQuery(product::class)
        )
    }

    @Test
    fun `building query works for class with no properties`() {
        class product

        assertEquals(
            "product {\n}",
            GraphQL.buildQuery(product::class)
        )
    }

    @Test
    fun `building query works for class with a class inside`() {
        data class subProduct(val id: Int, val name: String)
        data class product(val id: Int, val subProduct: subProduct)

        assertEquals(
            "product {\n\tid\n\tsubProduct {\n\tid\n\tname\n}\n}",
            GraphQL.buildQuery(product::class)
        )
    }

    @Test
    fun `building query works for class 2 level classes inside each other`() {
        data class subProduct2(val id: Int, val name: String)
        data class subProduct1(val id: Int, val subProduct2: subProduct2)
        data class product(val id: Int, val subProduct: subProduct1)

        assertEquals(
            "product {\n\tid\n\tsubProduct1 {\n\tid\n\tsubProduct2 {\n\tid\n\tname\n}\n}\n}",
            GraphQL.buildQuery(product::class)
        )
    }
}