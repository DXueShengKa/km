plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}


kotlin {
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

    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                api(kmLibs.kotlinx.serialization.json)
                api(kmLibs.kotlinx.serialization.protobuf)
                api(kmLibs.kotlinx.datetime)
            }
        }

        val jsMain by getting {
            dependencies {
//                implementation(kmLibs.kotlinx.serialization.runtime)
            }
        }

        val jvmMain by getting {
            dependencies {
//                implementation(kmLibs.kotlinx.serialization.runtime)
            }
        }

//        val jsTest by getting {
//            dependencies {
//                implementation(kotlin("test-js"))
//            }
//        }
    }
}
