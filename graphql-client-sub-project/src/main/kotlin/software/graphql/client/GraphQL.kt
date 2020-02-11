package software.graphql.client

fun query(vararg arguments: Argument, init: Query.() -> Unit) = Query(arguments).apply(init)

class Query() : NamedField() {
    init {
        fieldName = "query"
    }

    constructor(arguments: Array<out Argument>) : this() {
        this.arguments = arguments
    }

    fun <T : NamedField> initRoot(name: String, node: T, vararg arguments: Argument, init: T.() -> Unit) =
        initField(name, node, *arguments, init = init)

    fun initRoot(name: String, node: ScalarField, vararg arguments: Argument) =
        initField(name, node, *arguments)
}

interface Field {
    fun render(builder: StringBuilder, indent: String)

    fun <K> MutableMap<K, in String>.enquoteStrings() =
        this.apply {
            this.forEach { (key, value) ->
                if (value is String)
                    this[key] = "\"$value\""
            }
        }
}

@DslMarker
annotation class TypeMarker

@TypeMarker
abstract class NamedField : Field {
    protected lateinit var fieldName: String
    private val fields = arrayListOf<NamedField>()
    protected lateinit var arguments: Array<out Argument>

    protected fun <T : NamedField> initField(
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

    override fun render(builder: StringBuilder, indent: String) {
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

class ScalarField : NamedField() {
    override fun render(builder: StringBuilder, indent: String) {
        builder.append("$indent$fieldName${renderArguments()}\n")
    }
}

class Argument(private val name: String, private val value: Any?, private var defaultValue: Any? = null) {
    override fun toString() = when (value) {
        defaultValue -> ""
        is String -> "$name=\"$value\""
        else -> "$name=$value"
    }
}

abstract class Scalar {
    override fun toString() = render()
    abstract fun render(): String
}