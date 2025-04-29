package com.ertools.processing.model

data class ModelConfiguration(
    val modelName: String,
    val threadCount: Int,
    val useNNAPI: Boolean
)

