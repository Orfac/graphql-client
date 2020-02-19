package software.graphql.client

import java.util.*

class Fragment<T : Field> internal constructor(
    private val name: String,
    private val field: T
) : RenderableEntry() {

    override fun renderIndented(indent: String) =
        if (name.isEmpty())
            renderDeclaration(indent)
        else
            "$indent...$name\n"

    override fun validate() {
        if (!field.childrenRequested())
            throw QueryValidationException("Invalid fragment: no subfields specified")

        if (!nameValidOrEmpty(name))
            throw QueryValidationException("Invalid fragment: incorrect fragment name '$name'")
    }

    fun renderDeclaration(indent: String = "") =
        field.renderWithoutValidation(indent)

    // This is to use the same named fragment multiple times without redeclaring it
    override fun equals(other: Any?) =
        other is Fragment<*> && name.isNotEmpty() && other.name == name

    override fun hashCode() = name.hashCode() +
            if (name.isEmpty()) Random().nextInt()
            else 0
}

fun <T : Field> newFragment(
    name: String,
    constructor: (String, String, Collection<Argument<*>>) -> T,
    init: T.() -> Unit
): (Query) -> Fragment<T> {
    val className = constructor("", "", emptyList())::class.java.simpleName
    val field: T = constructor("fragment $name on $className", "", emptyList())

    val fragment = Fragment(name, field.apply(init))
    return { query ->
        query.declaredFragments += fragment
        fragment
    }
}

fun <T : Field> T.addInlineFragment(
    constructor: (String, String, Collection<Argument<*>>) -> T,
    init: T.() -> Unit
) {
    val className = constructor("", "", emptyList())::class.java.simpleName
    val field: T = constructor("... on $className", "", emptyList())
    addChild(Fragment("", field.apply(init)))
}

fun <T : Field> T.addFragment(fragment: Fragment<T>) = addChild(fragment)

