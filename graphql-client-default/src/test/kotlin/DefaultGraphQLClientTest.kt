import io.netty.handler.codec.http.HttpHeaderNames
import io.netty.handler.codec.http.HttpHeaders
import org.junit.Test
import reactor.core.publisher.Mono
import reactor.netty.ByteBufFlux
import reactor.netty.http.client.HttpClient
import software.graphql.client.QueryBuilder
import kotlin.test.assertEquals

const val defaultUrl = "https://countries.trevorblades.com/"

internal class DefaultGraphQLClientTest{

    data class country (val code : String = "", val name : String = "")
    data class data(val country : country? = null)
    data class response (val data : data? = null)
    @Test
    fun `sending and retrieving query works`() {
        val client = withDefaultClient()

        val query = "{\"operationName\":null,\"variables\":{},\"query\":\"{\\n  country {\\n    code\\n    name\\n  }\\n}\\n\"}"
        val extractedCountry = client.sendQuery<response>(query)

        assertEquals("response(data=data(country=country(code=null, name=null)))",extractedCountry.toString())
    }

    private fun withDefaultClient(): DefaultGraphQLClient {
        val nettyClient = HttpClient.create()
            .headers { h: HttpHeaders ->
                h[HttpHeaderNames.CONTENT_TYPE] = "application/json"
            }

        val webClient = DefaultGraphQLWebClient(nettyClient)
        return DefaultGraphQLClient(defaultUrl, webClient, DefaultGraphQLJsonConverter())
    }
}