plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
}

kotlin {
    jvm {

    }

    androidTarget {

    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.kotlin.deeplearning.tensorflow)
            implementation(libs.kotlin.deeplearning.onnx)
            implementation(libs.kotlin.deeplearning.visualization)
            implementation(libs.multik.core)
            implementation(libs.junit)
        }

        jvmMain.dependencies {
            implementation(libs.kotlin.deeplearning.tensorflow)
            implementation(libs.kotlin.deeplearning.onnx)
        }

        commonMain.dependencies {
            implementation(libs.kotlin.deeplearning.tensorflow)
            implementation(libs.kotlin.deeplearning.onnx)
            implementation(libs.kotlin.fasterxml.jackson.module)
            implementation(libs.multik.core)
        }
    }
}

android {
    namespace = "com.ertools.processing"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
        //testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "11"
        }
    }
}
