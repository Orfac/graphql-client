rootProject.name = "graphql-client"

include("graphql-client-sub-project")

val kotlinVersion: String by settings

pluginManagement {
    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "org.jetbrains.kotlin.jvm" -> useVersion(kotlinVersion)
            }
        }
    }
}
