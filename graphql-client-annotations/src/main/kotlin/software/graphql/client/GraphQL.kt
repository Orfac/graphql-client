package software.graphql.client

import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.jvmName

object GraphQL {
    fun buildQuery(someObject : KClass<*>) : String{
        var body : String = someObject.simpleName ?: ""
        body += " {\n"

        if (someObject.memberProperties.isEmpty()){
            throw IllegalArgumentException("There are no properties for a given class")
        }

        someObject.memberProperties.forEach {
            prop ->
            run { body += '\t' + buildProperty(prop) + '\n' }
        }
        body += "}"

        return body
    }

    private fun buildProperty(prop: KProperty1<out Any, Any?>): String {
        return if (!isPrimitiveType(prop))
            buildQuery(prop.returnType.classifier as KClass<*>)
            else prop.name
    }

    private fun isPrimitiveType(prop: KProperty1<out Any, Any?>) : Boolean{
        val className = (prop.returnType.classifier as? KClass<*>)?.simpleName ?: ""
        val primitives = arrayOf("Int", "String", "Double")
        return className == "" || className in primitives
    }



}