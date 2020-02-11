package software.graphql.client

fun query(vararg arguments: Argument, init: Query.() -> Unit) = Query(arguments).apply(init)

class Query() : Field() {
    init {
        fieldName = "query"
    }

    constructor(arguments: Array<out Argument>) : this() {
        this.arguments = arguments
    }

    fun <T : Field> initRoot(name: String, node: T, vararg arguments: Argument, init: T.() -> Unit) =
        initField(name, node, *arguments, init = init)

    fun initRoot(name: String, node: ScalarField, vararg arguments: Argument) =
        initField(name, node, *arguments)
}

@DslMarker
annotation class FieldMarker

@FieldMarker
abstract class Field {
    protected lateinit var fieldName: String
    private val fields = arrayListOf<Field>()
    protected lateinit var arguments: Array<out Argument>

    protected fun <T : Field> initField(
        name: String,
        node: T,
        vararg arguments: Argument,
        init: T.() -> Unit
    ) {
        node.fieldName = name
        node.arguments = arguments
        node.init()
        fields.add(node)
    }

    protected fun initField(
        name: String, node: ScalarField,
        vararg arguments: Argument
    ) {
        node.fieldName = name
        node.arguments = arguments
        fields.add(node)
    }

    protected open fun render(builder: StringBuilder, indent: String) {
        builder.append("$indent$fieldName${renderArguments()} {\n")
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
        is String -> "$name: \"$value\""
        else -> "$name: $value"
    }
}