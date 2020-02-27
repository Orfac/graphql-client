dependencies {
    compile(project(":graphql-client-core"))
    implementation("com.fasterxml.jackson.core:jackson-core:2.9.8")
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
}
