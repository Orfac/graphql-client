package software.graphql.client

import org.junit.Test
import kotlin.reflect.full.memberProperties
import kotlin.test.assertEquals

public class QueryTest{


    @Test
    public fun `building query works for simple class with 2 properties` (){
        data class Product(val id : Int, val name : String )

        val myProduct = Product(5,"pie")
        val query = Query()

        assertEquals(
            "Product {\n\tid\n\tname\n}",
            query.buildGraphQL(myProduct)
        )
    }

    @Test
    public fun `building query works for class with no properties`(){
        class Product()
        val myProduct = Product()
        val query = Query()
        assertEquals(
            "Product {}",
            query.buildGraphQL(myProduct)
        )
    }
}