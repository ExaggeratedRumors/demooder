package com.ertools.processing.model

data class ModelShape(
    val width: Int,
    val height: Int,
    val channels: Int,
    ) {
    companion object {
        fun fromShapeArray(shapeArray: IntArray): ModelShape {
            return ModelShape(
                height = shapeArray[1],
                width = shapeArray[2],
                channels = shapeArray[3]
            )
        }
    }

}