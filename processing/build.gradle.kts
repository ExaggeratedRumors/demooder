import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
}

kotlin {
    jvm {
        //compilerOptions.jvmTarget.set(JvmTarget.JVM_11)
    }

    androidTarget {

    }

    sourceSets {
        androidMain.dependencies {
            implementation (libs.kotlin.deeplearning.tensorflow)
            implementation (libs.kotlin.deeplearning.onnx)
            implementation (libs.kotlin.deeplearning.visualization)
        }

        jvmMain.dependencies {
            implementation (libs.kotlin.deeplearning.tensorflow)
            implementation (libs.kotlin.deeplearning.onnx)
        }

        commonMain.dependencies {
            implementation (libs.kotlin.deeplearning.tensorflow)
            implementation (libs.kotlin.deeplearning.onnx)
            implementation (libs.kotlin.fasterxml.jackson.module)
        }
    }
}

android {
    namespace = "com.ertools.demooder.processing"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}
