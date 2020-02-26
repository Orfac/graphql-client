package software.graphql.client.systemtest

import org.junit.Test
import software.graphql.client.*
import java.util.concurrent.TimeUnit
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

    @Test
    fun `queries from dsl are sent and treated correctly - jackson`() {
        assertEquals(
            data,
            sendQuery(JacksonObjectReader)
        )
    }

    @Test
    fun `queries from dsl are sent and treated correctly - gson`() {
        assertEquals(
            data,
            sendQuery(GsonObjectReader)
        )
    }
}

private fun sendQuery(jsonObjectReader: JsonObjectReader): Data {
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

    return GraphqlClient(
        NettyHttpSender,
        jsonObjectReader
    )
        .uri(COUNTRIES_GRAPHQL_URL)
        .sendQuery<Response>(query)
        .get(20, TimeUnit.SECONDS)
        .data!!
}
