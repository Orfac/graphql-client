import org.junit.Test
import reactor.netty.http.client.HttpClient
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull

internal class DefaultGraphQLWebClientTest {

    @Test
    fun `sending data works`() {
        val nettyClient = HttpClient.create()
        val webClient = DefaultGraphQLWebClient(nettyClient)
        val response = webClient.send("http://example.com/", "")
        assertNotNull(response)
        assertNotEquals("",response)
    }

    @Test
    fun `sending async data works`() {
        val nettyClient = HttpClient.create()
        val webClient = DefaultGraphQLWebClient(nettyClient)
        val response = webClient.sendReactive("http://example.com/", "").block()
        assertNotNull(response)
        assertNotEquals("",response)
    }
}