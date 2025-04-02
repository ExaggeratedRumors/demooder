plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
}

kotlin {
    jvm {
        //createTask("dataAugmentation", "com.ertools.processing.scripts.DataAugmentationScriptKt")
        //createTask("generateSpectrograms", "com.ertools.processing.scripts.SpectrogramsGeneratorScriptKt")
    }

    androidTarget {
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.multik.core)
            implementation(libs.junit)
        }

        jvmMain.dependencies {

        }

        jvmTest.dependencies {
            implementation(libs.junit)
        }

        commonMain.dependencies {
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
dependencies {
    testImplementation(libs.testng)
}

fun createTask(taskName: String, mainClassName: String) {
    tasks.register<JavaExec>(taskName) {
        group = "model tasks"
        description = "Run $mainClassName"
        mainClass.set(mainClassName)
        classpath = sourceSets["main"].runtimeClasspath
        workingDir = file("$rootDir")

        val argsProperty = project.findProperty("args")?.toString()
        if (argsProperty != null) {
            args = argsProperty.split("\\s+".toRegex())
        }
    }
}