plugins {
    application
    kotlin("jvm") version libs.versions.kotlin
}

kotlin {
    jvmToolchain(11)
}

group = "com.ertools"
version = "1.0-SNAPSHOT"

dependencies {
    /** Processing **/
    implementation(project(":processing"))

    /** DeepLearning **/
    implementation(libs.kotlin.deeplearning.tensorflow)
    implementation(libs.kotlin.deeplearning.onnx)
    implementation(libs.kotlin.deeplearning.visualization)

    /* GPU processing */
    implementation(libs.kotlin.libtensorflow)
    implementation(libs.kotlin.libtensorflow.jni.gpu)

    /** Serialization **/
    implementation(libs.kotlin.fasterxml.jackson.module)

    /** Logging **/
    implementation(libs.kotlin.org.slf4j.api)
    implementation(libs.kotlin.org.slf4j.simple)
}

fun createTask(taskName: String, mainClassName: String) {
    tasks.register<JavaExec>(taskName) {
        group = "model tasks"
        description = "Run $mainClassName"
        mainClass.set(mainClassName)
        classpath = sourceSets["main"].runtimeClasspath
        workingDir = file("$rootDir")
    }
}

createTask("dataAugmentation", "com.ertools.model.DataAugmentationMainKt")
createTask("createSpectrograms", "com.ertools.model.SpectrogramsMainKt")
createTask("trainModel", "com.ertools.model.TrainModelMainKt")
createTask("testModel", "com.ertools.model.TestModelMainKt")
createTask("predict", "com.ertools.model.PredictMainKt")
