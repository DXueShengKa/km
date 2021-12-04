import org.jetbrains.compose.compose
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")

    id("com.android.library")
    id("kotlin-parcelize")
}

kotlin {
    android()
    jvm()

    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(compose.material)
                implementation(project(":common:client"))
//                api(kmLibs.decompose.jetbrains)
                api("com.arkivanov.decompose:extensions-compose-jetbrains:${kmLibs.versions.decompose.get()}"){
                    exclude("org.jetbrains.compose.ui")
                    exclude("org.jetbrains.compose.foundation")
                }
            }
        }
    }
}

tasks.withType<KotlinJvmCompile>{
    kotlinOptions {
        jvmTarget = "11"
    }
}

android {

    compileSdk = kmLibs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = kmLibs.versions.minSdk.get().toInt()
        targetSdk = kmLibs.versions.targetSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    sourceSets {
        named("main") {
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
            res.srcDirs("src/androidMain/res",
                // "src/commonMain/resources"
            )
        }
    }
}
