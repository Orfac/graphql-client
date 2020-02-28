plugins {
    id("io.spring.dependency-management") version "1.0.7.RELEASE"
}
repositories {
    maven("https://repo.spring.io/release")
}
dependencies {
    compile(project(":graphql-client-core"))
    compile(project(":graphql-client-annotations"))
    compile("com.fasterxml.jackson.core:jackson-databind:2.10.2")
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
    compile("io.projectreactor.netty:reactor-netty")
    compile("io.projectreactor:reactor-bom:Californium-RELEASE")
}

dependencyManagement {
    imports {
        mavenBom("io.projectreactor:reactor-bom:Californium-RELEASE")
    }
}