package software.graphql.client

import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.jvmName

public class Query() {
    public fun buildGraphQL(someObject : Any) : String{
        var body : String = getClassName(someObject)
        body += " {\n"

        someObject :: class.memberProperties.forEach {
            prop -> body += '\t' + prop.name + '\n'
        }
        body += "}"
        return body
    }

    private fun getClassName(someObject: Any) : String{
        return (someObject :: class.jvmName)
            .split('.').last()
            .split('$').last()
    }
}