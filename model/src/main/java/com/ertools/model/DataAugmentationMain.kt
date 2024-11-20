package com.ertools.model

import com.ertools.model.augmentation.WavAugmentation
import com.ertools.processing.commons.ProcessingUtils

fun main(args: Array<String>) {

    /** Data **/
    val dataDir = if(args.isNotEmpty()) args[0] else  ProcessingUtils.DIR_CREMA_D


    /** Program **/
    println("I:\tStart augmentation audio data program.")

    println("I:\tData augmentation - increasing dataset ${ProcessingUtils.WAV_AUGMENT_AMOUNT} times.")
    val result = WavAugmentation.wavFileAugmentation(path = dataDir)
    println("R:\tCreated $result new wav files.")

    println("I:\tEnd program.")
}