package software.graphql.client

import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible

fun query(init: Query.() -> Unit) = Query().apply(init)

class Query : Type("query") {
    fun <T : Type> initRoot(node: T, init: T.() -> Unit) = initField(node, init)
    fun <T : ScalarType> initRoot(node: T) = initField(node) // Maybe don't need
}

interface IType {
    fun render(builder: StringBuilder, indent: String)
}

@DslMarker
annotation class TypeMarker

@TypeMarker
abstract class Type(val typeName: String) : IType {
    private val fields = arrayListOf<Type>()

    protected fun <T : Type> initField(node: T, init: T.() -> Unit) {
        node.init()
        fields.add(node)
    }

    protected fun <T : ScalarType> initField(node: T) {
        fields.add(node)
    }

    override fun render(builder: StringBuilder, indent: String) {
        builder.append("$indent$typeName${renderArguments()} {\n")
        for (c in fields)
            c.render(builder, "$indent  ")

        builder.append("$indent}\n")
    }

    protected fun renderArguments(): String {
        val builder = StringBuilder()

        val arguments = this::class.declaredMemberProperties
        val valueMap = hashMapOf<String, Any>()

        arguments.forEach { argument ->
            argument.getter.isAccessible = true
            argument.getter.call(this).takeIf { it != null }?.apply {
                if (this is String)
                    valueMap[argument.name] = "\"$this\""
                else
                    valueMap[argument.name] = this
            }
        }

        if (valueMap.isEmpty()) return ""

        builder.append(valueMap.entries
            .joinToString(prefix = "(", postfix = ")") { "${it.key}=${it.value}" })
        return builder.toString()
    }

    override fun toString() = StringBuilder().apply { render(this, "") }.toString()
}

open class ScalarType(name: String) : Type(name) {
    override fun render(builder: StringBuilder, indent: String) {
        builder.append("$indent$typeName${renderArguments()}\n")
    }
}