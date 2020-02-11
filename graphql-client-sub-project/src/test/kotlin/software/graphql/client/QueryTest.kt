package software.graphql.client

import org.junit.Test
import kotlin.reflect.full.memberProperties
import kotlin.test.assertEquals

public class QueryTest{

    public data class Product(val id : Int, val name : String )
    @Test
    public fun `building query works for simple class with 2 properties` (){
        val myProduct = Product(5,"pie")
        val query = Query()

        assertEquals(
            "Product {\n\tid\n\tname\n}",
            query.buildGraphQL(myProduct)
        )
    }
}