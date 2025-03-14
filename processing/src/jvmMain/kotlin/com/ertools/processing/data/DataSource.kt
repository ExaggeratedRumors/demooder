package com.ertools.processing.data

import com.ertools.processing.commons.Emotion
import com.ertools.processing.commons.ProjectPathing

sealed class DataSource(
    directoryName: String
) {
    companion object {
        fun sourceFromDirectoryName(directoryName: String): DataSource = when (directoryName) {
            ProjectPathing.DIR_DATA_CREMA_D -> CREMAD
            ProjectPathing.DIR_DATA_RAVDESS -> RAVDESS
            ProjectPathing.DIR_DATA_SAVEE -> SAVEE
            ProjectPathing.DIR_DATA_TESS -> TESS
            ProjectPathing.DIR_MY_RECORDS -> MyRecords
            else -> throw IllegalArgumentException("E: Unknown directory name: $directoryName.")
        }
    }

    val path: String = ProjectPathing.DIR_AUDIO_INPUT + "/$directoryName"
    abstract fun extractLabels(filename: String): Emotion?
}

data object CREMAD : DataSource(
    ProjectPathing.DIR_DATA_CREMA_D
) {
    override fun extractLabels(filename: String): Emotion? {
        val labelsDict = mapOf(
            "ANG" to Emotion.ANG,
            "DIS" to Emotion.DIS,
            "FEA" to Emotion.FEA,
            "HAP" to Emotion.HAP,
            "NEU" to Emotion.NEU,
            "SAD" to Emotion.SAD
        )
        try {
            val features = filename.split("_")
            if (features.size < 3) throw IllegalArgumentException()
            return features[2].let { labelsDict[it] }
        } catch (e: Exception) {
            println("E: Cannot extract labels from filename: $filename.")
            throw IllegalArgumentException()
        }
    }
}

data object RAVDESS : DataSource(
    ProjectPathing.DIR_DATA_RAVDESS
) {
    override fun extractLabels(filename: String): Emotion? {
        val labelsDict = mapOf(
            "01" to Emotion.NEU,
            "03" to Emotion.HAP,
            "04" to Emotion.SAD,
            "05" to Emotion.ANG,
            "06" to Emotion.FEA,
            "07" to Emotion.DIS
        )
        try {
            val features = filename.split("-")
            if (features.size < 3) throw IllegalArgumentException()
            return features[2].let { labelsDict[it] }
        } catch (e: Exception) {
            println("E: Cannot extract labels from filename: $filename.")
            throw IllegalArgumentException()
        }
    }
}

data object SAVEE : DataSource(
    ProjectPathing.DIR_DATA_SAVEE
) {
    override fun extractLabels(filename: String): Emotion? {
        val labelsDict = mapOf(
            "a" to Emotion.ANG,
            "d" to Emotion.DIS,
            "f" to Emotion.FEA,
            "h" to Emotion.HAP,
            "n" to Emotion.NEU,
            "sa" to Emotion.SAD,
        )
        try {
            val features = filename.replace(Regex("[0-9]"), "")
            return features.let { labelsDict[it] }
        } catch (e: Exception) {
            println("E: Cannot extract labels from filename: $filename.")
            throw IllegalArgumentException()
        }
    }
}

data object TESS : DataSource(
    ProjectPathing.DIR_DATA_TESS
) {
    override fun extractLabels(filename: String): Emotion? {
        val labelsDict = mapOf(
            "angry" to Emotion.ANG,
            "disgust" to Emotion.DIS,
            "fear" to Emotion.FEA,
            "happy" to Emotion.HAP,
            "neutral" to Emotion.NEU,
            "sad" to Emotion.SAD
        )
        try {
            val features = filename.split("-")
            if (features.size < 3) throw IllegalArgumentException()
            return features[2].let { labelsDict[it] }
        } catch (e: Exception) {
            println("E: Cannot extract labels from filename: $filename.")
            throw IllegalArgumentException()
        }
    }
}

data object MyRecords : DataSource(
    ProjectPathing.DIR_MY_RECORDS
) {
    override fun extractLabels(filename: String): Emotion? {
        val labelsDict = mapOf(
            "ANG" to Emotion.ANG,
            "DIS" to Emotion.DIS,
            "FEA" to Emotion.FEA,
            "HAP" to Emotion.HAP,
            "NEU" to Emotion.NEU,
            "SAD" to Emotion.SAD
        )
        try {
            val features = filename.split("_")
            if (features.size < 3) throw IllegalArgumentException()
            return features[2].let { labelsDict[it] }
        } catch (e: Exception) {
            println("E: Cannot extract labels from filename: $filename.")
            throw IllegalArgumentException()
        }
    }
}