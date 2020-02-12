package software.graphql.client

import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.jvmName

object GraphQL {
    fun buildGraphQL(someObject : KClass<*>) : String{
        var body : String = getClassName(someObject)
        body += " {\n"

        someObject.memberProperties.forEach {
            prop -> run {
                val isClass = (prop.returnType.classifier as? KClass<*>)?.isData ?: false
                val propString = if (isClass) buildGraphQL(prop.returnType.classifier as KClass<*>) else prop.name
                body += '\t' + propString + '\n'
            }
        }
        body += "}"
        return body
    }

    private fun getClassName(someObject: KClass<*>) : String{
        return someObject.jvmName
            .split('.').last()
            .split('$').last()
    }


}