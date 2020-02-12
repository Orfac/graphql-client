package software.graphql.client

fun query(queryName: String = "", vararg arguments: Argument, init: Query.() -> Unit) =
    Query(queryName, arguments).apply(init)

class Query(queryName: String) : Field() {
    private val createdFragments = arrayListOf<Fragment<*>>()

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

    fun <T : Field> createFragment(name: String, onType: T, onTypeName: String, init: T.() -> Unit): Fragment<T> {
        val fragment = Fragment(init, onType, onTypeName, name)
        createdFragments.add(fragment)
        return fragment
    }

    override fun render(builder: StringBuilder, indent: String) {
        builder.append("$indent$fieldName${renderArguments()} {\n")

        createdFragments.forEach { it.renderDeclaration(builder, "$indent  ") }
        renderFields(builder, "$indent  ")

        builder.append("$indent}\n")
    }
}

@DslMarker
annotation class FieldMarker

@FieldMarker
abstract class Field {
    lateinit var fieldName: String
    private lateinit var fieldAlias: String
    private val fields = arrayListOf<Field>()
    protected lateinit var arguments: Array<out Argument>
    protected val fragments = arrayListOf<Fragment<out Field>>()

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

    fun <T : Field> T.addFragment(fragment: Fragment<T>) {
        fragments.add(fragment)
    }

    fun <T : Field> T.addInlineFragment(onType: T, onTypeName: String, init: T.() -> Unit) {
        fragments.add(Fragment(init, onType, onTypeName))
    }

    protected open fun render(builder: StringBuilder, indent: String) {
        builder.append(indent)

        if (this::fieldAlias.isInitialized && fieldAlias.isNotEmpty())
            builder.append("$fieldAlias: ")

        builder.append("$fieldName${renderArguments()} {\n")
        renderFields(builder, "$indent  ")
        fragments.forEach { it.renderUsage(builder, "$indent  ") }
        builder.append("$indent}\n")
    }

    fun renderFields(builder: StringBuilder, indent: String) {
        for (c in fields)
            c.render(builder, indent)
    }

    fun renderArguments() =
        arguments.map(Argument::toString).filter { it.isNotEmpty() }
            .takeIf { it.isNotEmpty() }?.joinToString(prefix = "(", postfix = ")") { it } ?: ""

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

class Fragment<T : Field> internal constructor(
    val init: T.() -> Unit,
    private val onType: T,
    private val onTypeName: String,
    private val name: String = ""
) {
    private var initialized = false

    fun renderDeclaration(builder: StringBuilder, indent: String) {
        if (name.isEmpty()) {
            builder.append("$indent... on $onTypeName {\n")
        } else {
            builder.append("${indent}fragment $name on $onTypeName {\n")
        }
        if (!initialized) {
            onType.init()
            initialized = true
        }
        onType.renderFields(builder, "$indent  ")
        builder.append("$indent}\n")
    }

    fun renderUsage(builder: StringBuilder, indent: String) {
        if (name.isEmpty()) {
            renderDeclaration(builder, indent)
        } else {
            builder.append("$indent...$name\n")
        }
    }
}