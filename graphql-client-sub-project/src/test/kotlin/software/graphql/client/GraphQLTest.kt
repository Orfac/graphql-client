package software.graphql.client

import org.junit.Test
import kotlin.test.assertEquals

public class GraphQLTest {


    @Test
    public fun `building query works for simple class with 2 properties`() {
        data class Product(val id: Int, val name: String)

        assertEquals(
            "Product {\n\tid\n\tname\n}",
            GraphQL.buildQuery(Product::class)
        )
    }

    @Test
    public fun `building query works for class with no properties`() {
        class Product()

        assertEquals(
            "Product {\n}",
            GraphQL.buildQuery(Product::class)
        )
    }

    @Test
    public fun `building query works for class with a class inside`() {
        data class SubProduct(val id: Int, val name: String)
        data class Product(val id: Int, val subProduct: SubProduct)

        println(GraphQL.buildQuery(Product::class))
        assertEquals(
            "Product {\n\tid\n\tSubProduct {\n\tid\n\tname\n}\n}",
            GraphQL.buildQuery(Product::class)
        )
    }

    @Test
    public fun `building query works for class 2 level classes inside each other`() {
        data class SubProduct2(val id: Int, val name: String)
        data class SubProduct1(val id: Int, val subProduct2: SubProduct2)
        data class Product(val id: Int, val subProduct: SubProduct1)

        println(GraphQL.buildQuery(Product::class))
        assertEquals(
            "Product {\n\tid\n\tSubProduct1 {\n\tid\n\tSubProduct2 {\n\tid\n\tname\n}\n}\n}",
            GraphQL.buildQuery(Product::class)
        )
    }
}