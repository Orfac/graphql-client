import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
}

allprojects {
    group = "software.graphql.client"
}

subprojects {
    apply(plugin = "kotlin")

    repositories {
        mavenLocal()
        jcenter()
    }

    dependencies {
        implementation(kotlin("stdlib-jdk8"))
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = listOf("-Xjsr305=strict")
            allWarningsAsErrors = true
        }
    }
}
