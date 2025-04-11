package com.ertools.processing.model

data class ModelShape(
    val width: Int,
    val height: Int,
    val channels: Int,
    ) {
    companion object {
        fun fromShapeArray(shapeArray: IntArray): ModelShape {
            return ModelShape(
                width = shapeArray[0],
                height = shapeArray[1],
                channels = shapeArray[2]
            )
        }
    }

}