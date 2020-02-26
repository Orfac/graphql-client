package software.graphql.client.systemtest

import software.graphql.client.*

internal fun Query.country(code: String, fieldAlias: String = "", init: Country.() -> Unit) =
    initField(Country("country", fieldAlias, listOf(Argument("code", code))), init)

internal class Country(fieldName: String, fieldAlias: String = "", arguments: Collection<Argument<*>> = emptyList()) :
    Field(fieldName, fieldAlias, arguments) {

    fun code(fieldAlias: String = "") = initScalarField("code", fieldAlias)
    fun name(fieldAlias: String = "") = initScalarField("name", fieldAlias)
    fun native(fieldAlias: String = "") = initScalarField("native", fieldAlias)
    fun phone(fieldAlias: String = "") = initScalarField("phone", fieldAlias)
    fun currency(fieldAlias: String = "") = initScalarField("currency", fieldAlias)
    fun emoji(fieldAlias: String = "") = initScalarField("emoji", fieldAlias)
    fun emojiU(fieldAlias: String = "") = initScalarField("emojiU", fieldAlias)

    fun continent(fieldAlias: String = "", init: Continent.() -> Unit) =
        initField(Continent("continent", fieldAlias), init)

    fun languages(fieldAlias: String = "", init: Language.() -> Unit) =
        initField(Language("languages", fieldAlias), init)

    fun states(fieldAlias: String = "", init: State.() -> Unit) =
        initField(State("states", fieldAlias), init)
}

internal class Continent(fieldName: String, fieldAlias: String = "", arguments: Collection<Argument<*>> = emptyList()) :
    Field(fieldName, fieldAlias, arguments) {

    fun code(fieldAlias: String = "") = initScalarField("code", fieldAlias)
    fun name(fieldAlias: String = "") = initScalarField("name", fieldAlias)

    fun countries(fieldAlias: String = "", init: Country.() -> Unit) =
        initField(Country("countries", fieldAlias), init)
}

internal class Language(fieldName: String, fieldAlias: String = "", arguments: Collection<Argument<*>> = emptyList()) :
    Field(fieldName, fieldAlias, arguments) {

    fun code(fieldAlias: String = "") = initScalarField("code", fieldAlias)
    fun name(fieldAlias: String = "") = initScalarField("name", fieldAlias)
    fun native(fieldAlias: String = "") = initScalarField("native", fieldAlias)
    fun rtl(fieldAlias: String = "") = initScalarField("rtl", fieldAlias)
}

internal class State(fieldName: String, fieldAlias: String = "", arguments: Collection<Argument<*>> = emptyList()) :
    Field(fieldName, fieldAlias, arguments) {

    fun code(fieldAlias: String = "") = initScalarField("code", fieldAlias)
    fun name(fieldAlias: String = "") = initScalarField("name", fieldAlias)

    fun country(fieldAlias: String = "", init: Country.() -> Unit) =
        initField(Country("country", fieldAlias), init)
}