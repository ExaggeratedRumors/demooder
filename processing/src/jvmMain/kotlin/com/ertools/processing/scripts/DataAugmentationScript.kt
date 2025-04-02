package com.ertools.processing.scripts

import com.ertools.processing.augmentation.WavAugmentation
import com.ertools.processing.commons.ProcessingUtils

fun main(args: Array<String>) {
    if(args.isEmpty()) {
        println("E:\tNo arguments provided. Please provide the name of audio data directory (example: CREMA-D).")
        return
    }
    val audioDataPath = args[0]
    val augmentAmount = args.getOrNull(1)?.toIntOrNull() ?: ProcessingUtils.WAV_AUGMENT_AMOUNT

    /** Program **/
    println("I:\tStart augmentation audio data program.")

    println("I:\tData augmentation - increasing dataset $augmentAmount times.")
    val result = WavAugmentation.wavFilesAugmentation(path = audioDataPath, augmentFilesAmount = augmentAmount)
    println("R:\tCreated $result new wav files.")

    println("I:\tEnd program.")
}