dependencies {
    compile(project(":graphql-client-core"))
    compile("com.fasterxml.jackson.core:jackson-databind:2.10.2")
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
}
