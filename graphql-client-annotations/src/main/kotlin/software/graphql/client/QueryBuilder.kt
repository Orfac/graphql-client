package software.graphql.client

import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

object QueryBuilder {
    fun build(someObject : KClass<*>) : String{
        if (someObject.memberProperties.isEmpty()){
            throw IllegalArgumentException("There are no properties for a given class")
        }

        var body : String = someObject.simpleName ?: ""
        body += " {\n"

        someObject.memberProperties.forEach {
            prop ->
            run { body += '\t' + buildProperty(prop) + '\n' }
        }
        body += "}"

        return body
    }

    private fun buildProperty(prop: KProperty1<out Any, Any?>): String {
        return if (!isPrimitiveType(prop))
            build(prop.returnType.classifier as KClass<*>)
            else prop.name
    }

    private fun isPrimitiveType(prop: KProperty1<out Any, Any?>) : Boolean{
        val className = (prop.returnType.classifier as? KClass<*>)?.simpleName ?: ""
        val primitives = arrayOf("Int", "String", "Double")
        return className == "" || className in primitives
    }



}