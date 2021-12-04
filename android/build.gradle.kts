import org.jetbrains.compose.compose

plugins {
    kotlin("android")
    id("com.android.application")
    id("org.jetbrains.compose")
    id("kotlin-parcelize")
}

android {

    compileSdk = kmLibs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = kmLibs.versions.minSdk.get().toInt()
        targetSdk = kmLibs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
        applicationId = "com.km"
        multiDexEnabled = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions{
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(kmLibs.androidx.activity.compose)
    implementation(compose.material)
    implementation("org.webrtc:google-webrtc:1.0.32006")
    implementation(project(":common:client"))
    implementation(project(":common:compose-ui"))
}