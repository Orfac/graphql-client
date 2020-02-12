package software.graphql.client

import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.jvmName

public class Query() {
    public fun buildGraphQL(someObject : Any) : String{
        var body : String = getClassName(someObject)
        body += " {\n"

        someObject :: class.memberProperties.forEach {
            prop ->
            run {
                val isClass = (prop.returnType.classifier as? KClass<*>)?.isData ?: false
                val propString = if (isClass) buildQueryFromClass(prop.returnType.classifier as KClass<*>) else prop.name
                body += '\t' + propString + '\n'
            }
        }
        body += "}"
        return body
    }

    public fun buildQueryFromClass(someObject: KClass<*>) : String{
        var body : String = someObject.jvmName.split('.').last().split('$').last()
        body += " {\n"
        someObject :: memberProperties.get().forEach {
            prop ->
            run {
                val isClass = (prop.returnType.classifier as? KClass<*>)?.isData ?: false
                val propString: String
                propString = if (isClass){
                    val lol = prop.returnType.classifier as? KClass<*>
                    if (lol != null){
                        buildQueryFromClass(lol)
                    } else {
                        ""
                    }

                } else {
                    prop.name
                }

                body += '\t' + propString + '\n'
            }
        }
        body += "}"
        return body
    }

    private fun getClassName(someObject: Any) : String {
        return someObject :: class.jvmName
            .split('.').last()
            .split('$').last()
    }

    private fun getClassNameFromDeclaration(someObject: KClass<*>) : String{
        return someObject.jvmName
            .split('.').last()
            .split('$').last()
    }


}