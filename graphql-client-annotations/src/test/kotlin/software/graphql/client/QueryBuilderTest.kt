package software.graphql.client

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class QueryBuilderTest {


    @Test
    fun `building query works for simple class with 2 properties`() {
        data class product(val id: Int, val name: String)

        assertEquals(
            getTestQuery("product_with_id_and_name"),
            QueryBuilder.build(product::class)
        )
    }



    @Test
    fun `building query throws an exception for a class with no properties`() {
        class product
        assertFailsWith<IllegalArgumentException> {
            QueryBuilder.build(product::class)
        }
    }

    @Test
    fun `building query works for class with a class inside`() {
        data class subProduct(val id: Int, val name: String)
        data class product(val id: Int, val subProduct: subProduct)

        assertEquals(
            getTestQuery("product_with_sub_product"),
            QueryBuilder.build(product::class)
        )
    }

    @Test
    fun `building query works for class 2 level classes inside each other`() {
        data class subProduct2(val id: Int, val name: String)
        data class subProduct1(val id: Int, val subProduct2: subProduct2)
        data class product(val id: Int, val subProduct: subProduct1)

        assertEquals(
            getTestQuery("product_with_sub_products"),
            QueryBuilder.build(product::class)
        )
    }
}