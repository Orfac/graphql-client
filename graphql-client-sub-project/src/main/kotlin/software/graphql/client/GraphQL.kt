package software.graphql.client

fun query(queryName: String = "", vararg arguments: Argument, init: Query.() -> Unit) =
    Query(queryName, arguments).apply(init)

class Query(queryName: String) : Field() {
    init {
        fieldName = "query $queryName"
    }

    constructor(queryName: String, arguments: Array<out Argument>) : this(queryName) {
        this.arguments = arguments
    }

    fun <T : Field> initRoot(
        name: String,
        node: T,
        vararg arguments: Argument,
        init: T.() -> Unit
    ) =
        initField(name, node, *arguments, init = init)

    fun initRoot(
        name: String,
        node: ScalarField,
        vararg arguments: Argument
    ) =
        initField(name, node, *arguments)
}

@DslMarker
annotation class FieldMarker

@FieldMarker
abstract class Field {
    protected lateinit var fieldName: String
    private lateinit var fieldAlias: String
    private val fields = arrayListOf<Field>()
    protected lateinit var arguments: Array<out Argument>

    protected fun <T : Field> initField(
        name: String,
        node: T,
        vararg arguments: Argument,
        init: T.() -> Unit
    ): T {
        node.fieldName = name
        node.arguments = arguments
        node.init()
        fields.add(node)
        return node
    }

    protected fun initField(
        name: String, node: ScalarField,
        vararg arguments: Argument
    ): ScalarField {
        node.fieldName = name
        node.arguments = arguments
        fields.add(node)
        return node
    }

    infix fun String.alias(field: Field) {
        field.fieldAlias = this
    }

    protected open fun render(builder: StringBuilder, indent: String) {
        builder.append(indent)

        if (this::fieldAlias.isInitialized && fieldAlias.isNotEmpty())
            builder.append("$fieldAlias: ")

        builder.append("$fieldName${renderArguments()} {\n")

        for (c in fields)
            c.render(builder, "$indent  ")

        builder.append("$indent}\n")
    }

    fun renderArguments() =
        arguments.map(Argument::toString).filter { it.isNotEmpty() }
            .joinToString(prefix = "(", postfix = ")") { it }

    override fun toString() = StringBuilder().apply { render(this, "") }.toString()
}

class ScalarField : Field() {
    override fun render(builder: StringBuilder, indent: String) {
        builder.append("$indent$fieldName${renderArguments()}\n")
    }
}

class Argument(private val name: String, private val value: Any?, private var defaultValue: Any? = null) {
    override fun toString() = when (value) {
        defaultValue -> ""
        else -> "$name: ${renderValue(value)}"
    }

    companion object {
        private fun renderValue(data: Any?): String = when (data) {
            is String -> "\"$data\""
            is Collection<*> -> data.joinToString(prefix = "[", postfix = "]") { renderValue(it) }
            else -> data.toString()
        }
    }

}