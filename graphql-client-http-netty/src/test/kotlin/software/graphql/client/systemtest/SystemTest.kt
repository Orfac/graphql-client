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

    @Test
    fun `queries from dsl are sent and treated correctly - jackson, Mono`() {
        assertEquals(
            data,
            sendQuery(JacksonObjectReader, jacksonType())
                .asMono()
                .block(Duration.ofSeconds(10))!!
                .data
        )
    }

    @Test
    fun `queries from dsl are sent and treated correctly - gson, Mono`() {
        assertEquals(
            data,
            sendQuery(GsonObjectReader, gsonType())
                .asMono()
                .block(Duration.ofSeconds(10))!!
                .data
        )
    }

    @Test
    fun `queries from dsl are sent and treated correctly - gson, blocking`() {
        assertEquals(
            data,
            sendQuery(GsonObjectReader, gsonType()).call().data
        )
    }

    @Test
    fun `queries from dsl are sent and treated correctly - jackson, blocking`() {
        assertEquals(
            data,
            sendQuery(JacksonObjectReader, jacksonType()).call().data//.call().data
        )
    }

    @Test
    fun `context of Mono is saved`() {
        sendQuery(
            JacksonObjectReader,
            jacksonType()
        ).asMono()
            .subscriberContext { it.put(CONTEXT_TEST_KEY, CONTEXT_TEST_VALUE) }
            .subscriberContext { it.put(CONTEXT_TEST_ENABLED, true) }
            .block()
    }
}

private fun sendQuery(
    jsonObjectReader: JsonObjectReader,
    jsonTypeResolver: TypeResolver<GraphQLResponse<Data, PlainTextGraphQLError>>
): Callback<GraphQLResponse<Data, PlainTextGraphQLError>> {
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

    return NettyGraphQLClient(jsonObjectReader)
        .uri(COUNTRIES_GRAPHQL_URL)
        .sendQuery(query, jsonTypeResolver)
}
