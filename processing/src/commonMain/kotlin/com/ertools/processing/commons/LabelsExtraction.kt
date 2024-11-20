package com.ertools.processing.commons

import java.io.File
import org.jetbrains.kotlinx.dl.dataset.generator.LabelGenerator

object LabelsExtraction {
    fun fromLabel(label: String): Labels {
        try {
            val features = label.split("_")
            if (features.size < 4) throw IllegalArgumentException()
            return Labels(
                sentence = enumValueOf<Sentence>(features[1]),
                emotion = enumValueOf<Emotion>(features[2]),
                quality = enumValueOf<Quality>(features[3]),
                features.size > 4
            )
        } catch (e: Exception) {
            println("E: Cannot extract label $label.")
            throw IllegalArgumentException()
        }
    }

    fun getEmotionLabelGenerator() = object: LabelGenerator<File> {
        override fun getLabel(dataSource: File): Float {
            val emotion = fromLabel(dataSource.nameWithoutExtension).emotion
            return emotion.ordinal.toFloat()
        }
    }

    data class Labels(
        val sentence: Sentence,
        val emotion: Emotion,
        val quality: Quality,
        val fromAugmentation: Boolean
    )

    enum class Sentence {
        DFA, IEO, IOM, ITH, ITS, IWL, IWW, MTI, TAI, TIE, TSI, WSI
    }

    enum class Emotion {
        ANG, DIS, FEA, HAP, NEU, SAD
    }

    enum class Quality {
        XX, HI, LO, MD
    }
}