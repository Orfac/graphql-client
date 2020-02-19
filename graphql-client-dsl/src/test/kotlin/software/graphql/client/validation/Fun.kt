package software.graphql.client.validation

import software.graphql.client.*

fun Query.`fun`(alias: String = "", init: Fun.() -> Unit) = initField(Fun("fun", alias), init)
fun Query.`bad fun`(alias: String = "", init: Fun.() -> Unit) = initField(Fun("bad fun", alias), init)

class Fun(fieldName: String, alias: String, arguments: Collection<Argument<*>> = emptyList()) :
    Field(fieldName, alias, arguments) {

    fun name(alias: String = "") = initScalarField("name ", alias)
    fun numberName(alias: String = "") = initScalarField("№ame", alias)
    fun ageRecommendation(alias: String = "") = initScalarField(" ", alias)
    fun isAvailable(alias: String = "") = initScalarField("1s_Available", alias)

    fun badArgument(madness: Int, alias: String = "") =
        initScalarField("badArgument", alias, listOf(Argument("mad-ness", madness)))

    fun normalScalarField(alias: String = "") = initScalarField("normalScalarField", alias)
}