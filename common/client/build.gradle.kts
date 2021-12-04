import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("com.google.devtools.ksp")
    id("com.android.library")
    id("kotlin-parcelize")
}

kotlin {
    android()
    jvm()
    js(IR) {
        browser() {
            testTask {
                testLogging.showStandardStreams = true
                useKarma {
                    useChromiumHeadless()
                }
            }
        }
        binaries.executable()
    }

    sourceSets {
        named("commonMain") {
            dependencies {
                implementation(compose.runtime)
                implementation(kotlin("stdlib-common"))
                implementation(kmLibs.kotlinx.coroutines.core)
                api(kmLibs.koin.core)
                with(kmLibs.ktorClient){
                    api(core)
                    api(serialization)
                    api(websockets)
                    implementation(logging)
                }
                api(project(":common:logic"))

                api(kmLibs.decompose)
            }
        }
        named("jsMain") {
            dependencies {

            }
        }

        named("jvmMain") {
            dependencies {
                implementation(kmLibs.ktorClient.java)
            }
        }

        named("androidMain") {
            dependencies {
                implementation(kmLibs.ktorClient.okhttp)
                api(kmLibs.koin.android)
            }
        }
    }
}

dependencies{
    ksp(project(":km-processor"))
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
            res.srcDirs("src/androidMain/res")
        }
    }
}
