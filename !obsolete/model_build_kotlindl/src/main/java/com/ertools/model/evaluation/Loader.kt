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

fun getEmotionLabelGenerator() = object: LabelGenerator<File> {
    override fun getLabel(dataSource: File): Float {
        val emotion = fromLabel(dataSource.nameWithoutExtension).emotion
        return emotion.ordinal.toFloat()
    }
}