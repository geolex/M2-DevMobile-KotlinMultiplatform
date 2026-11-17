import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    kotlin("plugin.serialization") version "2.4.0"
    id("com.github.gmazzo.buildconfig") version "5.5.1"
}

kotlin {
    jvm()
    android {
       namespace = "com.adrien.kmp_multiplatform.shared"
       compileSdk = libs.versions.android.compileSdk.get().toInt()
       minSdk = libs.versions.android.minSdk.get().toInt()
       compilerOptions {
           jvmTarget = JvmTarget.JVM_11
       }
       androidResources {
           enable = true
       }
       withHostTest {
           isIncludeAndroidResources = true
       }
       withDeviceTestBuilder {
           sourceSetTreeName = "test"
       }.configure {
           instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
       }
    }
    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.compose.uiTooling)
            implementation("io.ktor:ktor-client-okhttp:3.4.2")
        }
        iosMain.dependencies {
            implementation("io.ktor:ktor-client-darwin:3.4.2")
        }
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            implementation("io.ktor:ktor-client-core:3.4.2")
            implementation("io.ktor:ktor-client-content-negotiation:3.4.2")
            implementation("io.ktor:ktor-serialization-kotlinx-json:3.4.2")
            implementation("io.ktor:ktor-client-logging:3.4.2")

            implementation("org.jetbrains.compose.material:material-icons-extended:1.7.3")
            implementation("org.jetbrains.androidx.lifecycle:lifecycle-viewmodel-compose:2.11.+")

            implementation("io.coil-kt.coil3:coil-network-ktor3:3.2.0")
            implementation("io.coil-kt.coil3:coil-compose:3.2.0")

            implementation("org.jetbrains.androidx.navigation3:navigation3-ui:1.1.2")

            implementation("io.insert-koin:koin-compose:4.1.+")
            implementation("io.insert-koin:koin-compose-viewmodel:4.1.+")
            implementation("io.insert-koin:koin-compose-viewmodel-navigation:4.1.+")
        }
        jvmMain.dependencies {
            implementation("io.ktor:ktor-client-okhttp:3.4.2")
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        getByName("androidDeviceTest").dependencies {
            implementation(libs.androidx.testRunner)
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
}

// Read API key from local.properties
val localProperties = Properties() //import java.utils
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}

buildConfig {
    // Définit le nom de la classe générée
    className("BuildConfig")
    // Le package où la classe sera générée
    packageName("org.example.project")

    // Récupération sécurisée de la clé
    val weatherAPIKey = localProperties.getProperty("weather.api.key") ?: ""

    println("weatherAPIKey chargée : $weatherAPIKey")

    // Crée le champ pour tous les targets (Android, iOS, Desktop)
    buildConfigField("String", "WEATHER_API_KEY", "\"$weatherAPIKey\"")
}