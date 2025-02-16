@file:Suppress("PropertyName")

plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "com.ertools.demooder"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.ertools.demooder"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
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
        jvmTarget = "11"
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
    implementation(libs.androidx.datastore.preferences)

    /** Processing **/
    implementation(project(":processing"))

    /** Compose **/
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.runtime)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3.adaptive.navigation.suite)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material3.window.size.clazz)
    implementation(libs.androidx.graphics.shapes.android)
    implementation(libs.androidx.navigation.compose)

    /** ViewModel **/
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    /** Accompanist **/
    implementation(libs.accompanist.permissions)
    implementation(libs.accompanist.systemuicontroller)

    /** DeepLearning **/
    implementation (libs.kotlin.deeplearning.tensorflow)
    implementation (libs.kotlin.deeplearning.onnx)
    implementation (libs.kotlin.deeplearning.visualization)
    //implementation (libs.kotlin.libtensorflow.jni.gpu)

    /** Complex **/
    implementation(libs.multik.core)
    implementation(libs.androidx.junit.ktx)

    /** Test **/
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.core.ktx)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.androidx.runner)
    androidTestImplementation(libs.androidx.rules)
    androidTestImplementation(libs.androidx.uiautomator)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}