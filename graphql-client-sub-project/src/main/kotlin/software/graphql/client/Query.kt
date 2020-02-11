package software.graphql.client

import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.jvmName

public class Query() {
    public fun buildGraphQL(someObject : Any) : String{
        var body : String = (someObject :: class.jvmName).split('$')[1]

        body += " {\n"

        someObject :: class.memberProperties.forEach {
            prop -> body += '\t' + prop.name + '\n'
        }
        body += "}"
        println(body)
        return body
    }
}