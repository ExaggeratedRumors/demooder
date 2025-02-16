package com.ertools.processing.io


import com.ertools.processing.commons.ImageDim
import com.ertools.processing.commons.LabelsExtraction
import com.ertools.processing.commons.ProcessingUtils
import com.ertools.processing.commons.ProcessingUtils.SPECTROGRAM_COLOR_RANGE
import com.ertools.processing.signal.SignalPreprocessor.convertStftToAmplitude
import com.ertools.processing.spectrogram.SpectrogramSample
import com.ertools.processing.spectrogram.SpectrogramsMetadata
import com.fasterxml.jackson.databind.ObjectMapper
import org.jetbrains.kotlinx.dl.api.core.shape.TensorShape
import org.jetbrains.kotlinx.dl.api.inference.TensorFlowInferenceModel
import org.jetbrains.kotlinx.dl.api.preprocessing.Operation
import org.jetbrains.kotlinx.dl.dataset.Dataset
import org.jetbrains.kotlinx.dl.dataset.OnFlyImageDataset
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import javax.sound.sampled.AudioFileFormat
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem
import kotlin.random.Random

object IOManager {
    private val objectMapper = ObjectMapper().apply { findAndRegisterModules() }

    /***************/
    /** Wav files **/
    /***************/
    fun fetchWavFiles(
        wavDir: String,
        maxNumberOfFiles: Int = Int.MAX_VALUE
    ): List<File> {
        return File(ProcessingUtils.DIR_AUDIO_INPUT, wavDir)
            .listFiles { _, name -> name.endsWith(".${ProcessingUtils.EXT_WAV_FILE}") }
            ?.take(maxNumberOfFiles) ?: emptyList()
    }

    fun loadWavFiles(
        wavDir: String,
        maxNumberOfFiles: Int = Int.MAX_VALUE
    ): List<WavFile> = fetchWavFiles(wavDir, maxNumberOfFiles)
        .map { WavFile(it) }

    fun saveWavFile(stream: AudioInputStream, dir: String, filename: String) {
        AudioSystem.write(
            stream,
            AudioFileFormat.Type.WAVE,
            File("${ProcessingUtils.DIR_AUDIO_INPUT}/$dir", "$filename.${ProcessingUtils.EXT_WAV_FILE}")
        )
    }


    /******************/
    /** Spectrograms **/
    /******************/
    fun getMetadataDim(dir: String): ImageDim {
        val metadata: SpectrogramsMetadata = loadSpectrogramMetadata(dir)
        val dim = ImageDim(width = metadata.timeSizeMin, height = metadata.freqSizeMin)
        return dim
    }

    fun saveSpectrogramMetadata(metadata: SpectrogramsMetadata, dir: String) {
        val file = File("${ProcessingUtils.DIR_SPECTROGRAMS_OUTPUT}/$dir", ProcessingUtils.FILE_SPECTROGRAMS_METADATA)
        saveObject(file, metadata)
    }

    fun loadSpectrogramMetadata(dir: String): SpectrogramsMetadata {
        val file = File("${ProcessingUtils.DIR_SPECTROGRAMS_OUTPUT}/$dir", ProcessingUtils.FILE_SPECTROGRAMS_METADATA)
        return loadObject(file, SpectrogramsMetadata::class.java)
    }

    fun saveSpectrogramSamples(set: List<SpectrogramSample>, dir: String) {
        set.forEach { saveSpectrogramSample(it, dir) }
    }

    fun saveSpectrogramSample(sample: SpectrogramSample, dir: String) {
        val image = complexArrayToPng(sample)
        val fileDir = File("${ProcessingUtils.DIR_SPECTROGRAMS_OUTPUT}/$dir")
        if(!fileDir.exists()) fileDir.mkdir()
        val file = File(
            "${ProcessingUtils.DIR_SPECTROGRAMS_OUTPUT}/$dir",
            "${sample.filename}.${ProcessingUtils.EXT_SPECTROGRAM_OUTPUT}"
        )
        ImageIO.write(image, ProcessingUtils.EXT_SPECTROGRAM_OUTPUT, file)
    }

    fun complexArrayToPng(sample: SpectrogramSample): BufferedImage {
        val magnitudeMatrix = sample.data.convertStftToAmplitude()

        /* Create image */
        val width = magnitudeMatrix.size
        val height = magnitudeMatrix[0].size

        val maxIntensity = magnitudeMatrix.maxOf { it.max() }
        val minIntensity = magnitudeMatrix.minOf { it.min() }

        val image = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)

        for (x in 0 until width) {
            for (y in 0 until height) {
                val intensity = magnitudeMatrix[x][y]
                val normalizedIntensity = ((intensity - minIntensity) / (maxIntensity - minIntensity) * SPECTROGRAM_COLOR_RANGE).toInt()
                val color = Color(normalizedIntensity, normalizedIntensity, normalizedIntensity).rgb
                image.setRGB(x, height - y - 1, color)
            }
        }
        return image
    }

    /*************/
    /** Dataset **/
    /*************/
    fun loadDataset(
        dir: String,
        shuffle: Boolean = false,
        pipeline: (ImageDim) -> (Operation<BufferedImage, Pair<FloatArray, TensorShape>>)
    ): Pair<OnFlyImageDataset<File>, ImageDim> {
        val metadata: SpectrogramsMetadata = loadSpectrogramMetadata(dir)
        val dim = ImageDim(width = metadata.timeSizeMin, height = metadata.freqSizeMin)
        val dataset = OnFlyImageDataset.create(
            pathToData = File("${ProcessingUtils.DIR_SPECTROGRAMS_OUTPUT}/$dir"),
            labelGenerator = LabelsExtraction.getEmotionLabelGenerator(),
            preprocessing = pipeline.invoke(dim)
        ).also { dataset ->
            if (shuffle) (0 until Random.nextInt(ProcessingUtils.DATASET_SHUFFLE_ATTEMPTS)).forEach { _ -> dataset.shuffle() }
        }
        return Pair(dataset, dim)
    }

    fun loadAndSplitDataset(
        dir: String,
        testDataRatio: Double = ProcessingUtils.DATASET_TEST_RATIO,
        validDataRatio: Double = ProcessingUtils.DATASET_VALID_RATIO,
        pipeline: (ImageDim) -> (Operation<BufferedImage, Pair<FloatArray, TensorShape>>)
    ): Triple<Dataset, Dataset, Dataset> {
        val (dataset, _) = loadDataset(dir, true, pipeline)
        val (preTrain, test) = dataset.split(1 - testDataRatio)
        val (train, valid) = preTrain.split(1 - validDataRatio)
        return Triple(train, test, valid)
    }

    /***********/
    /** Model **/
    /***********/
    fun loadModel(modelPath: String) = TensorFlowInferenceModel.load(File(modelPath))


    /*************/
    /** Private **/
    /*************/
    private fun <T>saveObject(file: File, data: T) = objectMapper.writeValue(file, data)

    private fun <T>loadObject(file: File, objectClass: Class<T>): T = objectMapper.readValue(file, objectClass)
}