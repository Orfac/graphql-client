package software.graphql.client

import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.jvmName

object GraphQL {
    fun buildQuery(someObject : KClass<*>) : String{
        var body : String = getClassName(someObject)
        body += " {\n"
        someObject.memberProperties.forEach {
            prop ->
            run { body += '\t' + buildProperty(prop) + '\n' }
        }
        body += "}"

        return body
    }

    private fun getClassName(someObject: KClass<*>) : String{
        return someObject.jvmName
            .split('.').last()
            .split('$').last()
    }

    private fun buildProperty(prop: KProperty1<out Any, Any?>): String {
        // Checking if property is data class
        val isDataClass = (prop.returnType.classifier as? KClass<*>)?.isData ?: false
        return if (isDataClass)
            buildQuery(prop.returnType.classifier as KClass<*>)
            else prop.name
    }




}