plugins {
    id("io.spring.dependency-management") version "1.0.7.RELEASE"
}

repositories {
    maven("https://repo.spring.io/release")
}

dependencies {
    compile(project(":graphql-client-http"))
    compile("com.fasterxml.jackson.core:jackson-databind:2.10.2")
}

dependencyManagement {
    imports {
        mavenBom("io.projectreactor:reactor-bom:Californium-RELEASE")
    }
}