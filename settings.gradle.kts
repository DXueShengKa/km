@file:Suppress("UnstableApiUsage")

rootProject.name = "km"

enableFeaturePreview("VERSION_CATALOGS")

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

}

dependencyResolutionManagement {
//    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

include(
    ":common:logic",
    ":common:client",
    ":common:compose-ui",
    ":server", ":android", ":web",":desktop",
    ":km-processor"
)

dependencyResolutionManagement {
    versionCatalogs {
        create("kmLibs") {
            kmLibs()
        }
    }
}

fun VersionCatalogBuilder.kmLibs() {
    //开始 ---------------------------  版本号 -------------------------------  开始
    version("compose", "1.0.0")
    version("kotlin", "1.5.31")
    version("ksp", "1.5.31-1.0.0")
    version("kotlinxCoroutines", "1.5.2")
    version("kotlinxSerialization", "1.3.0")
    version("kotlinxDatetime", "0.3.0")
    //kotlin js扩展包装库
    version("kotlinWrappersBom","0.0.1-pre.261-kotlin-1.5.31")
    version("ktor", "1.6.4")
    version("springBoot", "2.5.4")
    //多平台导航库
    version("decompose", "0.4.0")
    //多平台依赖注入
    version("koin", "3.1.3")

    version("npm-muiMaterial","5.0.4")
    version("npm-muiLab","5.0.0-alpha.51")
    version("npm-react","17.0.2")
    version("npm-reactDom","17.0.2")
    version("npm-emotionReact","11.4.1")
    version("npm-emotionStyled","11.3.0")
    //结束 ---------------------------  版本号 -------------------------------  结束


    //开始 ---------------------------  kotlin -------------------------------  开始
    alias("kotlin-stdlib-jdk8").to("org.jetbrains.kotlin","kotlin-stdlib-jdk8").versionRef("kotlin")
    alias("kotlin-reflect").to("org.jetbrains.kotlin","kotlin-reflect").versionRef("kotlin")

    alias("kotlinx-coroutines-reactor").to("org.jetbrains.kotlinx","kotlinx-coroutines-reactor").versionRef("kotlinxCoroutines")
    alias("kotlinx-coroutines-core").to("org.jetbrains.kotlinx","kotlinx-coroutines-core").versionRef("kotlinxCoroutines")

    alias("kotlinx-serialization-json").to("org.jetbrains.kotlinx","kotlinx-serialization-json").versionRef("kotlinxSerialization")
    alias("kotlinx-serialization-protobuf").to("org.jetbrains.kotlinx","kotlinx-serialization-protobuf").versionRef("kotlinxSerialization")
    alias("kotlinx-datetime").to("org.jetbrains.kotlinx","kotlinx-datetime").versionRef("kotlinxDatetime")
    //结束 ---------------------------  kotlin -------------------------------  结束


    //开始 ---------------------------  ktor-client -------------------------------  开始
    alias("ktorClient-core").to("io.ktor","ktor-client-core").versionRef("ktor")
    alias("ktorClient-java").to("io.ktor","ktor-client-java").versionRef("ktor")
    alias("ktorClient-android").to("io.ktor","ktor-client-android").versionRef("ktor")
    alias("ktorClient-okhttp").to("io.ktor","ktor-client-okhttp").versionRef("ktor")
    alias("ktorClient-auth").to("io.ktor","ktor-client-auth").versionRef("ktor")
    alias("ktorClient-serialization").to("io.ktor","ktor-client-serialization").versionRef("ktor")

    alias("ktorClient-logging").to("io.ktor","ktor-client-logging").versionRef("ktor")
    alias("ktorClient-websockets").to("io.ktor","ktor-client-websockets").versionRef("ktor")
    //结束 ---------------------------  ktor-client -------------------------------  结束



    //开始 ---------------------------  springBoot -------------------------------  开始
    alias("springframeworkBoot").toPluginId("org.springframework.boot").versionRef("springBoot")

    alias("springBoot-starter-data-jpa").to("org.springframework.boot","spring-boot-starter-data-jpa").versionRef("springBoot")
    alias("springBoot-starter-jdbc").to("org.springframework.boot","spring-boot-starter-jdbc").versionRef("springBoot")
    alias("springBoot-starter-mail").to("org.springframework.boot","spring-boot-starter-mail").versionRef("springBoot")
    alias("springBoot-starter-oauth2-client").to("org.springframework.boot","spring-boot-starter-oauth2-client").versionRef("springBoot")
    alias("springBoot-starter-oauth2-resourceServer").to("org.springframework.boot","spring-boot-starter-oauth2-resource-server").versionRef("springBoot")
    alias("springBoot-starter-security").to("org.springframework.boot","spring-boot-starter-security").versionRef("springBoot")
    alias("springBoot-starter-web").to("org.springframework.boot","spring-boot-starter-web").versionRef("springBoot")
    alias("springBoot-starter-web-services").to("org.springframework.boot","spring-boot-starter-web-services").versionRef("springBoot")
    alias("springBoot-starter-webflux").to("org.springframework.boot","spring-boot-starter-webflux").versionRef("springBoot")
    alias("springBoot-starter-websocket").to("org.springframework.boot","spring-boot-starter-websocket").versionRef("springBoot")
    alias("springBoot-starter-aop").to("org.springframework.boot","spring-boot-starter-aop").versionRef("springBoot")
    alias("springBoot-starter-data-redis").to("org.springframework.boot","spring-boot-starter-data-redis").versionRef("springBoot")

    bundle("springBootStarter", listOf(
        "springBoot-starter-data-jpa",
        "springBoot-starter-data-redis",
        "springBoot-starter-jdbc",
        "springBoot-starter-mail",
//        "springBoot-starter-oauth2-client",
//        "springBoot-starter-oauth2-resourceServer",
//        "springBoot-starter-security",
//        "springBoot-starter-web",
//        "springBoot-starter-web-services",
        "springBoot-starter-webflux",
        "springBoot-starter-websocket",
        "springBoot-starter-aop"
    ))

    alias("springBoot-devtools").to("org.springframework.boot","spring-boot-devtools").versionRef("springBoot")
    alias("springBoot-configuration-processor").to("org.springframework.boot","spring-boot-configuration-processor").versionRef("springBoot")
    alias("springBoot-test").to("org.springframework.boot","spring-boot-test").versionRef("springBoot")

    alias("springframework-security-test").to("org.springframework.security","spring-security-test").version("5.5.1")
    //结束 ---------------------------  springBoot -------------------------------  结束



    //开始 ---------------------------  android -------------------------------  开始
    version("androidToolsBuild","7.0.0")
    version("compileSdk","31")
    version("minSdk","26")
    version("targetSdk","31")

    alias("androidToolsBuild").to("com.android.tools.build","gradle").version("7.0.0")
    alias("androidx-activity-compose").to("androidx.activity","activity-compose").version("1.3.1")
    //结束 ---------------------------  android -------------------------------  结束



    //开始 ---------------------------  ksp -------------------------------  开始
    alias("ksp-plugin").to("com.google.devtools.ksp","symbol-processing-gradle-plugin").versionRef("ksp")
    alias("ksp-api").to("com.google.devtools.ksp","symbol-processing-api").versionRef("ksp")
    alias("kotlinpoet").to("com.squareup","kotlinpoet").version("1.10.2")
    //结束 ---------------------------  ksp -------------------------------  结束


    //开始 ---------------------------  koin -------------------------------  开始
    alias("koin-core").to("io.insert-koin","koin-core").versionRef("koin")
    alias("koin-android").to("io.insert-koin","koin-android").versionRef("koin")
    //结束 ---------------------------  koin -------------------------------  结束


    //开始 ---------------------------  decompose -------------------------------  开始
    alias("decompose").to("com.arkivanov.decompose","decompose").versionRef("decompose")
    alias("decompose-jetbrains").to("com.arkivanov.decompose","extensions-compose-jetbrains").versionRef("decompose")
    //结束 ---------------------------  decompose -------------------------------  结束


    //开始 ---------------------------  其他 -------------------------------  开始
    alias("jetbrains-compose").to("org.jetbrains.compose","compose-gradle-plugin").versionRef("compose")
    alias("zxingCore").to("com.google.zxing", "core").version("3.4.1")
    alias("mysql-connector").to("mysql","mysql-connector-java").version("8.0.25")

}

