plugins {
    id("io.spring.dependency-management") version "1.0.7.RELEASE"
}
repositories {
    maven("https://repo.spring.io/release")
}
dependencies {
    compile("io.projectreactor.netty:reactor-netty")
    compile("io.projectreactor:reactor-bom:Californium-RELEASE")
}

dependencyManagement {
    imports {
        mavenBom("io.projectreactor:reactor-bom:Californium-RELEASE")
    }
}