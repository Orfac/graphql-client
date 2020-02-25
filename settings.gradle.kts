
rootProject.name = "graphql-client"


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
include("graphql-client-core")
include("graphql-client-dsl")
include("graphql-client-annotations")
