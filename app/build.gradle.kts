@file:Suppress("PropertyName")

val compose_ui_version: String by project
val compose_material_version: String by project
val compose_material3_version: String by project
val compose_animation_version: String by project
val compose_buildr_version: String by project
val compose_foundation_version: String by project
val compose_runtime_version: String by project

plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "com.ertools.demooder"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.ertools.demooder"
        minSdk = 31
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        compileSdkPreview = "UpsideDownCake"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
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
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }
}

dependencies {
    /** Core **/
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    /** Compose **/
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.runtime)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3.adaptive.navigation.suite)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material3.window.size.clazz)

    /** Accompanist **/
    implementation(libs.accompanist.permissions)
    implementation(libs.accompanist.systemuicontroller)

    /** DeepLearning **/
    implementation (libs.kotlin.deeplearning.tensorflow)
    implementation (libs.kotlin.deeplearning.onnx)
    implementation (libs.kotlin.deeplearning.visualization)

    /** Navigation **/
    implementation(libs.androidx.navigation.compose)

    /** Processing **/
    //implementation(project(":processing"))

    /** Test **/
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}