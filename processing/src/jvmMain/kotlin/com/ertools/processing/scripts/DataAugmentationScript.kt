package com.ertools.processing.scripts

import com.ertools.processing.augmentation.WavAugmentation
import com.ertools.processing.commons.ProcessingUtils
import com.ertools.processing.commons.ProjectPathing

fun main(args: Array<String>) {
    /** Data **/
    val dataDirs = if(args.isNotEmpty()) args.toList()
    else listOf(
        ProjectPathing.DIR_DATA_CREMA_D,
        ProjectPathing.DIR_DATA_RAVDESS,
        ProjectPathing.DIR_DATA_SAVEE,
        ProjectPathing.DIR_DATA_TESS
    )

    /** Configuration **/
    val augmentAmount = ProcessingUtils.WAV_AUGMENT_AMOUNT

    /** Program **/
    println("I:\tStart augmentation audio data program.")

    for(dataDir in dataDirs) {
        println("\n\nI:\tLoad sound data from directory: $dataDir")
        println("I:\tData augmentation - increasing dataset $augmentAmount times.")
        val result = WavAugmentation.wavFilesAugmentation(path = dataDir, augmentFilesAmount = augmentAmount)
        println("R:\tCreated $result new wav files.")

    }
    println("I:\tEnd program.")
}