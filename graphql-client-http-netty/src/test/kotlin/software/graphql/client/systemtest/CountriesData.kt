package software.graphql.client.systemtest

import software.graphql.client.GraphQLResponse
import software.graphql.client.PlainTextGraphQLError

// All properties are nullable vars with default values to ease the construction of data by json-parsing tools
// Actually, for jackson it would be enough to provide default values, and gson doesn't need that at all
internal data class Response(
    override val data: Data? = null,
    override val errors: Collection<PlainTextGraphQLError>? = null,
    override val extensions: Nothing? = null
) : GraphQLResponse

internal data class Data(var country: CountryData? = null)

internal data class CountryData(
    var code: String? = null, var name: String? = null,
    var native: String? = null, var phone: String? = null,
    var continent: ContinentData? = null, var currency: String? = null,
    var languages: Collection<LanguageData?>? = null, var emoji: String? = null,
    var emojiU: String? = null, var states: Collection<StateDate?>? = null
)

internal data class ContinentData(
    var code: String? = null, var name: String? = null,
    var country: Collection<CountryData>? = null
)

internal data class LanguageData(
    var code: String? = null, var name: String? = null,
    var native: String? = null, var rtl: Int? = null
)

internal data class StateDate(
    var code: String? = null, var name: String? = null,
    var country: CountryData? = null
)