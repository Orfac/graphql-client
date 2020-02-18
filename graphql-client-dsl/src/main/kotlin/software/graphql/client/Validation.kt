package software.graphql.client

fun validateName(name: String) = name.matches(Regex("[_A-Za-z][_0-9A-Za-z]*"))

fun validateField(fieldName: String, fieldAlias: String) =
    validateName(fieldName) && (validateName(fieldAlias) || fieldAlias.isEmpty())
