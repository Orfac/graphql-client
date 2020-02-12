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
            "Product {\n}",
            query.buildGraphQL(myProduct)
        )
    }

    @Test
    public fun `building query works for class with a class inside`(){
        data class SubProduct(val id : Int, val name : String)
        data class Product(val id : Int, val subProduct: SubProduct )

        val myProduct = Product(5,SubProduct(1,"pie"))
        val query = Query()
        println(query.buildGraphQL(myProduct))
        assertEquals(
            "Product {\n\tid\n\tSubProduct {\n\tid\n\tname\n}\n}",
            query.buildGraphQL(myProduct)
        )
    }

    @Test
    public fun `building query works for class 2 level classes inside each other`(){
        data class SubProduct2(val id : Int, val name : String)
        data class SubProduct1(val id : Int, val subProduct2: SubProduct2)
        data class Product(val id : Int, val subProduct: SubProduct1 )

        val myProduct = Product(5,SubProduct1(1,SubProduct2(1,"pie")))
        val query = Query()
        println(query.buildGraphQL(myProduct))
        assertEquals(
            "Product {\n\tid\n\tSubProduct1 {\n\tid\n\tSubProduct2 {\n\tid\n\tname\n}\n}\n}",
            query.buildGraphQL(myProduct)
        )
    }
}