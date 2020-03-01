plugins {
    id("io.spring.dependency-management") version "1.0.7.RELEASE"
}

repositories {
    maven("https://repo.spring.io/release")
}

dependencies {
    compile(project(":graphql-client-dsl"))

    implementation("org.slf4j:slf4j-simple:1.7.30")
    compile("io.projectreactor.netty:reactor-netty")

    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))

    testImplementation(project(":graphql-client-jackson"))
    testCompile(project(":graphql-client-gson"))

}

dependencyManagement {
    imports {
        mavenBom("io.projectreactor:reactor-bom:Californium-RELEASE")
    }
}