import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    dependencies {
        classpath("org.springframework.boot:spring-boot-gradle-plugin:${kmLibs.versions.springBoot.get()}")
    }
}

@Suppress("DSL_SCOPE_VIOLATION") // 抑制idea目前版本的bug
plugins {
    alias(kmLibs.plugins.springframeworkBoot)
    kotlin("jvm")
    kotlin("kapt")
//    id("org.springframework.boot") version kmLibs.versions.springBoot.get()
    kotlin("plugin.spring") version kmLibs.versions.kotlin.get()
    kotlin("plugin.jpa") version kmLibs.versions.kotlin.get()
    kotlin("plugin.serialization")
}

group = "com.km"
version = "0.1"

java{
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}


tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    implementation(kmLibs.bundles.springBootStarter)

    implementation(kmLibs.kotlinx.coroutines.reactor)
    implementation(kmLibs.kotlin.stdlib.jdk8)
    implementation(kmLibs.kotlin.reflect)

    implementation(project(":common:logic"))

    developmentOnly(kmLibs.springBoot.devtools)
    runtimeOnly(kmLibs.mysql.connector)
    kapt(kmLibs.springBoot.configuration.processor)
    testImplementation(kmLibs.springBoot.test)


    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions:1.1.4")
    testImplementation("io.projectreactor:reactor-test:3.4.9")

    testImplementation(kmLibs.springframework.security.test)
}


tasks.withType<Test> {
    useJUnitPlatform()
}
