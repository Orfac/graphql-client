package software.graphql.client.validation

import software.graphql.client.Argument
import software.graphql.client.Field
import software.graphql.client.initScalarField

fun query(init: Query.() -> Unit) = Query().apply(init)

class Query : Field("query", "") {
    fun `fun`(alias: String = "", init: Fun.() -> Unit) = initField(Fun("fun", alias), init)
    fun `bad fun`(alias: String = "", init: Fun.() -> Unit) = initField(Fun("bad fun", alias), init)
}

class Fun(fieldName: String, alias: String, vararg arguments: Argument<*>) : Field(fieldName, alias, *arguments) {
    fun name(alias: String = "") = initScalarField("name ", alias)
    fun numberName(alias: String = "") = initScalarField("№ame", alias)
    fun ageRecommendation(alias: String = "") = initScalarField(" ", alias)
    fun isAvailable(alias: String = "") = initScalarField("1s_Available", alias)

    fun badArgument(madness: Int, alias: String = "") =
        initScalarField("badArgument", alias, Argument("mad-ness", madness))

    fun normalScalarField(alias: String = "") = initScalarField("normalScalarField", alias)
}