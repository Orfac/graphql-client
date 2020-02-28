import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Test
import reactor.core.publisher.Mono
import kotlin.test.assertNotNull


//class data(val country : Country)
//class response (val data : data)

internal class DefaultGraphQLJsonConverterTest {

    data class Country (val code : String = "", val name : String = "")
    @Test
    fun `deserialization works for non reactive method`() {

        val jsonString = "{\"code\":\"asd\",\"name\":\"asd\"}"
        val converter = DefaultGraphQLJsonConverter()
        val parsedObject = converter.deserialize(jsonString, Country :: class.java)
        assertNotNull(parsedObject)
    }

    @Test
    fun `deserialization works for reactive method`() {

        val jsonString = Mono.just("{\"code\":\"asd\",\"name\":\"asd\"}")
        val converter = DefaultGraphQLJsonConverter()
        val parsedObject = converter.deserializeReactive(jsonString, Country :: class.java).block()
        assertNotNull(parsedObject)
    }
}