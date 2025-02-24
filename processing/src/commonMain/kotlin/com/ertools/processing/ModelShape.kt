package com.ertools.processing

data class ModelShape(
    val width: Long,
    val height: Long,
    val channels: Long,
    ) {
    companion object {
        fun fromShapeArray(shapeArray: LongArray): ModelShape {
            return ModelShape(
                width = shapeArray[0],
                height = shapeArray[1],
                channels = shapeArray[2],
                )
        }
    }

}