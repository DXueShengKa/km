

buildscript {

    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

    dependencies {

        classpath(kmLibs.jetbrains.compose)
        val kotlinVersions =  kmLibs.versions.kotlin.get()
        classpath(kotlin("gradle-plugin",kotlinVersions))
        classpath(kotlin("serialization",kotlinVersions))
        classpath(kmLibs.ksp.plugin)
        classpath(kmLibs.androidToolsBuild)
    }
}

