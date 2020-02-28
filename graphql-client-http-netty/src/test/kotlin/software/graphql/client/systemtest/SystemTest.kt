package software.graphql.client.systemtest

import org.junit.Test
import software.graphql.client.*
import java.time.Duration
import kotlin.test.assertEquals

private const val COUNTRIES_GRAPHQL_URL = "https://countries.trevorblades.com/"

internal class SystemTest {

    private val data =
        Data(
            CountryData(
                name = "Russia",
                phone = "7",
                continent = ContinentData(name = "Europe"),
                currency = "RUB",
                languages = listOf(LanguageData(name = "Russian")),
                emojiU = "U+1F1F7 U+1F1FA"
            )
        )
//
//    @Test
//    fun `queries from dsl are sent and treated correctly - jackson, CompletableFuture`() {
//        assertEquals(
//            data,
//            sendQuery(JacksonObjectReader)
//                .asCompletableFuture()
//                .get(10, TimeUnit.SECONDS)!!
//                .data
//        )
//    }

    @Test
    fun `queries from dsl are sent and treated correctly - jackson, Mono`() {
        assertEquals(
            data,
            sendQuery(JacksonObjectReader)
                .asMono()
                .block(Duration.ofSeconds(10))!!
                .data
        )
    }
//
//    @Test
//    fun `queries from dsl are sent and treated correctly - gson, CompletableFuture`() {
//        assertEquals(
//            data,
//            sendQuery(GsonObjectReader)
//                .asCompletableFuture()
//                .get(10, TimeUnit.SECONDS)
//                .data
//        )
//    }

    @Test
    fun `queries from dsl are sent and treated correctly - gson, Mono`() {
        assertEquals(
            data,
            sendQuery(GsonObjectReader)
                .asMono()
                .block(Duration.ofSeconds(10))!!
                .data
        )
    }

    @Test
    fun `queries from dsl are sent and treated correctly - gson, blocking`() {
        assertEquals(
            data,
            sendQuery(GsonObjectReader).call().data
        )
    }
}

private fun sendQuery(jsonObjectReader: JsonObjectReader): GraphQLCallback<GraphQLResponse<Data>> {
    val query = query {
        country(code = "RU") {
            name()
            phone()
            continent {
                name()
            }
            currency()
            languages {
                name()
            }
            emojiU()
        }
    }

    return GraphQLClient(
        NettyHttpSender,
        jsonObjectReader
    )
        .uri(COUNTRIES_GRAPHQL_URL)
        .sendQuery(query)
}
