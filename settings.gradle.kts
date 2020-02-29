rootProject.name = "graphql-client"

include("graphql-client-dsl")
include("graphql-client-http")
include("graphql-client-http-netty")
include("graphql-client-jackson")
include("graphql-client-gson")

include("graphql-client-annotations")

val kotlinVersion: String by settings

pluginManagement {
    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "org.jetbrains.kotlin.jvm" -> useVersion("1.3.61")
            }
        }
    }
}
