plugins {
    id("io.spring.dependency-management") version "1.0.7.RELEASE"
}

repositories {
    maven("https://repo.spring.io/release")
}

dependencies {
    compile(project(":graphql-client-http"))
    compile("com.google.code.gson:gson:2.8.6")
}

dependencyManagement {
    imports {
        mavenBom("io.projectreactor:reactor-bom:Californium-RELEASE")
    }
}