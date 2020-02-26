package software.graphql.client

import org.junit.Test
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals

internal class SystemTest {

    private val fullVersion = VersionData("1.5.106", "1.0.1-b138R")
    private val partialVersion = VersionData("1.5.106", null)

    @Test
    fun `full queries from dsl are sent and treated correctly - jackson`() {
        assertEquals(
            fullVersion,
            sendFullQuery(JacksonObjectReader)
        )
    }

    @Test
    fun `partial queries from dsl are sent and treated correctly - jackson`() {
        assertEquals(
            partialVersion,
            sendPartialQuery(JacksonObjectReader)
        )
    }

    @Test
    fun `full queries from dsl are sent and treated correctly - gson`() {
        assertEquals(
            fullVersion,
            sendFullQuery(GsonObjectReader)
        )
    }

    @Test
    fun `partial queries from dsl are sent and treated correctly - gson`() {
        assertEquals(
            partialVersion,
            sendPartialQuery(GsonObjectReader)
        )
    }
}

private fun sendQuery(jsonObjectReader: JsonObjectReader, query: Query) =
    GraphqlClient(NettyHttpSender, jsonObjectReader)
        .uri("http://graph-api.pre.spb.play.dc/graphql")
        .sendQuery<Response>(query)
        .get(5, TimeUnit.SECONDS)
        .data!!
        .version!!

private fun sendFullQuery(jsonObjectReader: JsonObjectReader): VersionData {
    val query = query {
        version {
            graphModel()
            graphqlSchema()
        }
    }
    return sendQuery(jsonObjectReader, query)
}

private fun sendPartialQuery(jsonObjectReader: JsonObjectReader): VersionData {
    val query = query {
        version {
            graphModel()
        }
    }
    return sendQuery(jsonObjectReader, query)
}

// All properties are nullable vars with default values to ease the construction of data by json-parsing tools
// Actually, for jackson it would be enough to provide default values, and gson doesn't need that at all
private data class Response(var data: Data? = null)

private data class Data(var version: VersionData? = null)
private data class VersionData(var graphModel: String? = null, var graphqlSchema: String? = null)

private fun Query.version(fieldAlias: String = "", init: Version.() -> Unit) =
    initField(Version("version", fieldAlias), init)

private class Version(fieldName: String, fieldAlias: String, arguments: Collection<Argument<*>> = emptyList()) :
    Field(fieldName, fieldAlias, arguments) {

    fun graphModel(fieldAlias: String = "") = initScalarField("graphModel", fieldAlias)
    fun graphqlSchema(fieldAlias: String = "") = initScalarField("graphqlSchema", fieldAlias)
}