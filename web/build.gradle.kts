import org.jetbrains.compose.compose


plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    kotlin("plugin.serialization")
}

version = "0.1"

kotlin {

    js(IR) {

        browser {
            webpackTask {
                cssSupport.enabled = true
            }

            runTask {
                cssSupport.enabled = true
            }

            testTask {
                useKarma {
                    useChromiumHeadless()
                    webpackConfig.cssSupport.enabled = true
                }
            }
        }
        binaries.executable()
    }


    sourceSets {

        named("jsMain") {
            dependencies {
                implementation(project(":common:client"))
                implementation(compose.runtime)
                implementation(compose.web.core)

                implementation(project.dependencies.enforcedPlatform(
                    kotlinWrappers("wrappers-bom:${kmLibs.versions.kotlinWrappersBom.get()}")
                ))

                arrayOf("react", "react-dom", "styled", "css-js", "mui").forEach {
                    implementation(kotlinWrappers(it))
                }

                with(kmLibs.versions.npm) {
                    implementation(npm("@mui/material", muiMaterial.get()))
                    implementation(npm("@mui/lab", muiLab.get()))
                    implementation(npm("react", react.get()))
                    implementation(npm("react-dom", reactDom.get()))
                    implementation(npm("@emotion/react", emotionReact.get()))
                    implementation(npm("@emotion/styled", emotionStyled.get()))
                }

            }
        }

    }

}

// a temporary workaround for a bug in jsRun invocation - see https://youtrack.jetbrains.com/issue/KT-48273
afterEvaluate {
    rootProject.extensions.configure<org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension> {
        versions.webpackDevServer.version = "4.0.0"
        versions.webpackCli.version = "4.9.0"
    }
}