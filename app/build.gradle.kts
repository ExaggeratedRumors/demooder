plugins {
    id("com.android.application")
    kotlin("android")
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.ertools.demooder"
    compileSdkPreview = "UpsideDownCake"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.ertools.demooder"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

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
}

dependencies {
    /** Core **/
    implementation(libs.androidx.datastore.preferences)

    /** Processing **/
    implementation(project(":processing"))

    /** Compose **/
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3.adaptive.navigation.suite)
    implementation(libs.androidx.graphics.shapes.android)
    implementation(libs.androidx.navigation.compose)

    /** Tensorflow **/
    implementation(libs.litert)

    /** Complex **/
    implementation(libs.multik.core)

    /** Test **/
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.core.ktx)
    androidTestImplementation(libs.androidx.ui.test.junit4)

}