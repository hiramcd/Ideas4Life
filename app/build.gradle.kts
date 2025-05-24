import java.util.Properties
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")
    kotlin("kapt")
    kotlin("plugin.serialization") version "2.1.21"
    id("com.google.dagger.hilt.android")
}
android.buildFeatures.buildConfig = true
val localProperties = Properties().apply {
    load(rootProject.file("local.properties").inputStream())
}
val openAiApiKey = localProperties["OPENAI_API_KEY"] as String
val prompt = localProperties["PROMPT"] as String
android {

    namespace = "h.ideas4life"
    compileSdk = 35

    packaging {
        resources {
            excludes += "/META-INF/gradle/incremental.annotation.processors"
        }
    }
    defaultConfig {
        applicationId = "h.ideas4life"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "OPENAI_API_KEY", "\"$openAiApiKey\"")
        buildConfigField("String", "PROMPT", "\"$prompt\"")

    }

    configurations.implementation {
        exclude(group = "com.intellij", module = "annotations")
    }

    buildTypes {
        getByName("debug") {
            buildConfigField("String", "OPENAI_API_KEY", "\"$openAiApiKey\"")
            buildConfigField("String", "PROMPT", "\"$prompt\"")
        }
        getByName("release") {
            buildConfigField("String", "OPENAI_API_KEY", "\"$openAiApiKey\"")
            buildConfigField("String", "PROMPT", "\"$prompt\"")
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }


}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.androidx.media3.common.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.material3.lint)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.retrofit)
    implementation(libs.retrofit2.kotlinx.serialization.converter)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.hilt.android)
    implementation(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.hilt.compiler)
    kapt(libs.hilt.android.compiler)


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}